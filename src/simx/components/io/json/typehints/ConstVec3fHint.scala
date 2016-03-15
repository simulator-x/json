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

import org.json4s._
import org.json4s.jackson.Serialization
import simplex3d.math.float.ConstVec3
import simplex3d.math.floatx.Vec3f

/**
 *
 * Created by dennis on 15.05.14.
 */
object ConstVec3fHint extends StackableHint[ConstVec3]("v3"){
  def main(args: Array[String]) {
    implicit val hint = TypeHints.shortHints(Nil, ConstVec3fHint)
    println(Serialization.read(Serialization.write(Vec3f.UnitX.toConst)))
  }

  protected val serialize: PartialFunction[ConstVec3, JObject] = {
    case mat: ConstVec3 => JObject(
//    JField("x", JDouble(mat.x)), JField("y", JDouble(mat.y)), JField("z", JDouble(mat.z))
    JField("v",JArray(JFloat(mat.x) :: JFloat(mat.y) :: JFloat(mat.z) :: Nil))
//    JField("x", JFloat(mat.x)), JField("y", JFloat(mat.y)), JField("z", JFloat(mat.z))
    )
  }


  protected val deserialize: PartialFunction[JObject, ConstVec3] = {
//    case JObject(List( JField("x", x : JNumber), JField("y", y : JNumber), JField("z", z : JNumber) )) =>
    case JObject(List( JField("v", JArray( JFloat(x) :: JFloat(y) :: JFloat(z) :: Nil) ))) =>
      ConstVec3( x, y, z )
  }
}