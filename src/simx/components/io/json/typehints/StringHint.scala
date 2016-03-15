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

package simx.components.io.json.typehints

import org.json4s.JsonAST.{JNumber, JField, JObject}
import org.json4s._
import simplex3d.math.float._

/**
 *
 * Created by dennis on 12.06.14.
 */
object StringHint extends StackableHint[String]{
  protected val serialize : PartialFunction[String, JObject] = {
    case str : String => JObject(JField("value", JString(str)))
  }

  protected val deserialize : PartialFunction[JObject, String] = {
    case JObject(List( JField("value", str : JString))) => str.s
  }
}
