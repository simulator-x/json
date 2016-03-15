/*
 * Copyright 2015 The SIRIS Project
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

package simx.components.io.json.typehints

import java.nio.{ByteBuffer, ByteOrder}

import org.json4s._

/**
 *
 * Created by dennis on 25.08.15.
 */

object JFloat{
  val byteOrder = ByteOrder.LITTLE_ENDIAN
  val identifyingChar = ' '

  def main(args: Array[String]) {
    val f = 1.23423423424234234f
    if(f == JFloat(JFloat(f)))
      println("passed")
    else
      println("failed")
  }

  private val buffer = ByteBuffer allocate 4 order byteOrder
  private val offset = 1

  // encode
  private def float2String (value : Float) = {
    buffer.rewind()
    buffer.putFloat(value).rewind()
    new String(Array[Char](identifyingChar, buffer.getChar, buffer.getChar))
  }

  def apply(f : Float) : JString =
    JString(float2String(f))

  /// decode
  def apply(f : JString) : Float = {
    buffer.rewind()
    if (f.s.charAt(0) == identifyingChar) {
      buffer.putChar(f.s.charAt(offset))
      buffer.putChar(f.s.charAt(offset + 1))
    } else if (f.s.charAt(0) == '-'){
      buffer.putChar(f.s.charAt(offset).^(0xFF).toChar)
      buffer.putChar(f.s.charAt(offset + 1))
    } else if (f.s.charAt(0) == '+'){
      buffer.putChar(f.s.charAt(offset))
      buffer.putChar(f.s.charAt(offset + 1).^(0xFF).toChar)
    } else if (f.s.charAt(0) == '='){
    buffer.putChar(f.s.charAt(offset).^(0xFF).toChar)
    buffer.putChar(f.s.charAt(offset + 1).^(0xFF).toChar)
  }
    buffer.getFloat(0)
  }

  def unapply(f : JString) : Option[Float] =
    Some(apply(f))
}
