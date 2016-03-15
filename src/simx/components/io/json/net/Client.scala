/*
 * Copyright 2014 The SIRIS Project
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * The SIRIS Project is a cooperation between Beuth University, Berlin and the
 * HCI Group at the University of Würzburg. The project is funded by the German
 * Federal Ministry of Education and Research (grant no. 17N4409).
 */

package simx.components.io.json.net

import java.net.InetSocketAddress
import java.nio.charset.StandardCharsets
import akka.actor.{Actor, Props, ActorRef}
import akka.util.ByteString

/**
 *
 * Created by dennis on 13.05.14.
 */
object Client {
  def props(remote: InetSocketAddress, replies: ActorRef) =
    Props(classOf[Client], remote, replies)

  def getEncoding =
    StandardCharsets.UTF_8
}

case class JSONString(str : String)
case class Disconnected(graceful : Boolean)
case class Disconnect()
case class WriteFailed()

class Client(address: InetSocketAddress, listener: ActorRef) extends Actor {
  import akka.io.{ IO, Tcp }
  import context.system
  import Tcp._

  val manager = IO(Tcp)
  manager ! Connect(address)

  def receive = {
    case CommandFailed(_: Connect) =>
      listener ! Disconnected(graceful = false)
      context stop self

    case c @ Connected(remote, local) =>
      val connection = sender()
      listener ! c
      connection ! Register(self)
      context become {
        case data: ByteString =>
          val len = data.size
          val numBits = (math.log(len)/math.log(2)).toInt
          val arr = Array[Byte](0, 0, 0, 0)
          for (i <- 0 until numBits/8+1)
            arr(arr.length-i-1) = ((len >> (i*8)) % 256).toByte
          connection ! Write( ByteString(arr) ++ data)
        case CommandFailed(w: Write) =>
          // O/S buffer was full
          listener ! WriteFailed()
        case Received(data) =>
          handleIncoming(data, listener)
        case Disconnect() =>
          connection ! Close
        case _: ConnectionClosed =>
          listener ! Disconnected(graceful = true)
          context stop self
      }
  }

  private def parseHeader(b : ByteString) : Int =
    if (b.size < 4)
      throw new Exception("contents to short to parse header")
    else
      (0 until 4).foldLeft(0) { (l, r) => (l << 8) + (if (b(r) < 0) 256+b(r) else b(r) )}

  private var buffer = ByteString()

  private def handleIncoming(data : ByteString, listener : ActorRef) {
    buffer = buffer concat data
    while (buffer.size >=4 ){
      val len = parseHeader(buffer)
      if (buffer.size >= len + 4) {
        buffer = buffer.drop(4)
        val (toHandle, remaining) = buffer.splitAt(len)
        listener ! JSONString(toHandle.decodeString(Client.getEncoding.name()))
        buffer = remaining
      } else
        return
    }
  }
}