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

import org.json4s.JsonAST.{JField, JObject}
import org.json4s._

/**
 *
 * Created by Benjamin Eckstein on 16.09.14.
 */
object JSonFloatHint extends StackableHint[JSonFloat]{
  protected val serialize : PartialFunction[JSonFloat, JObject] = {
    case fl : JSonFloat => JObject(JField("value", JDouble(fl.value.toDouble)))
  }

  protected val deserialize : PartialFunction[JObject, JSonFloat] = {
    case JObject(List( JField("value", fl : JDouble))) => JSonFloat(fl.values.toFloat)
    case JObject(List( JField("value", fl : JInt))) => JSonFloat(fl.values.toFloat)
    case JObject(List( JField("value", fl : JDecimal))) => JSonFloat(fl.values.toFloat)
  }
}
