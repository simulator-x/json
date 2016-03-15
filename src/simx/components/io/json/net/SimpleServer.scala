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

import akka.actor._
import java.net.InetSocketAddress
import akka.util.ByteString
import simx.components.io.json.typehints.{ConstMat4fHint, TypeHints}

import simplex3d.math.float._
import org.json4s.jackson.Serialization._

/**
 *
 * Created by dennis on 13.05.14.
 */
object SimpleServer {

  case class Send(str : String)

  class MyHandler extends Actor{
    override def receive: Receive = {
      case "connection closed" =>
        println("closed")
        context stop self
        context.system.shutdown()
      case "connect failed" =>
        println("failed")
        context stop self
        context.system.shutdown()
      case msg : ByteString =>
        println(msg.utf8String)
      case msg =>
        println(msg)
    }
  }

  def main(args: Array[String]) {
    val myActorSystem = ActorSystem.create("testSystem")
    val client = myActorSystem.actorOf(Client.props(new InetSocketAddress("localhost", 8000), myActorSystem.actorOf(Props(classOf[MyHandler]))))

    implicit val hint = TypeHints.shortHints(ConstMat4fHint)

    var matToWrite = ConstMat4(Mat4x3.translate(Vec3.UnitY))
    while (!myActorSystem.isTerminated){
      matToWrite = ConstMat4(Mat4x3.rotateZ(Math.PI.toFloat/10f)) * matToWrite
      client ! ByteString(write(matToWrite))
      Thread.sleep(16)
    }

  }

}