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

import simplex3d.math.float._
import org.json4s._
import org.json4s.JsonAST.JNumber

/**
 * Created by dwiebusch on 17.05.14
 */

object ConstQuat4fHint extends StackableHint[ConstQuat4]("q4"){
  protected val serialize: PartialFunction[ConstQuat4, JObject] = {
    case quat: ConstQuat4 => JObject(
//      JField("x", JDouble(quat.a)), JField("y", JDouble(quat.b)), JField("z", JDouble(quat.c)), JField("w", JDouble(quat.d))
        JField("v", JArray(JFloat(quat.a) :: JFloat(quat.b) :: JFloat(quat.c) :: JFloat(quat.d) :: Nil))
    )
  }

  protected val deserialize: PartialFunction[JObject, ConstQuat4] = {
//    case JObject( List(JField("x", a : JNumber), JField("y", b : JNumber), JField("z", c : JNumber), JField("w", d : JNumber))) =>
    case JObject( List(JField("v", JArray( JFloat(a) :: JFloat(b) :: JFloat(c) :: JFloat(d) :: Nil )) )) =>
      ConstQuat4( a, b, c, d )
  }
}
