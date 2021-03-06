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

package simx.components.io.json.settings

import org.json4s.jackson.JsonMethods
import com.fasterxml.jackson.core._
import io.{SerializedString, CharacterEscapes}
import JsonParser.Feature

/**
 *
 * Created by dennis on 25.08.15.
 */
object JacksonSettings {
  JsonMethods.mapper.getFactory.setCharacterEscapes( new CharacterEscapes {
    override def getEscapeSequence(ch: Int): SerializableString =
      new SerializedString(new String(Array[Int](ch), 0, 1))

    override def getEscapeCodesForAscii: Array[Int] =
      new Array[Int](128)
  })

  JsonMethods.mapper.setConfig(
    JsonMethods.mapper.getDeserializationConfig.`with`(Feature.ALLOW_UNQUOTED_CONTROL_CHARS)
  )

  def init(): Unit = {}
}
