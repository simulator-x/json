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

import java.nio.ByteBuffer

import org.json4s._
import org.json4s.jackson.Serialization
import simplex3d.math.float.ConstMat4
import simplex3d.math.floatx.Mat4x3f

/**
 * Created by dwiebusch on 12.05.14
 */
object ConstMat4fHint extends StackableHint[ConstMat4]{
  protected val serialize : PartialFunction[ConstMat4, JObject] = {
    case mat : ConstMat4 => JObject(
      JField("0", JFloat(mat.m00)), JField("1", JFloat(mat.m01)), JField("2", JFloat(mat.m02)), JField("3", JFloat(mat.m03)),
      JField("4", JFloat(mat.m10)), JField("5", JFloat(mat.m11)), JField("6", JFloat(mat.m12)), JField("7", JFloat(mat.m13)),
      JField("8", JFloat(mat.m20)), JField("9", JFloat(mat.m21)), JField("A", JFloat(mat.m22)), JField("B", JFloat(mat.m23)),
      JField("C", JFloat(mat.m30)), JField("D", JFloat(mat.m31)), JField("E", JFloat(mat.m32)), JField("F", JFloat(mat.m33))
    )
  }

  protected val deserialize : PartialFunction[JObject, ConstMat4] = {
    case JObject(List(
      JField("0", JFloat(m00)), JField("1", JFloat(m01)), JField("2", JFloat(m02)), JField("3", JFloat(m03)),
      JField("4", JFloat(m10)), JField("5", JFloat(m11)), JField("6", JFloat(m12)), JField("7", JFloat(m13)),
      JField("8", JFloat(m20)), JField("9", JFloat(m21)), JField("A", JFloat(m22)), JField("B", JFloat(m23)),
      JField("C", JFloat(m30)), JField("D", JFloat(m31)), JField("E", JFloat(m32)), JField("F", JFloat(m33))
    )) => ConstMat4(
      m00, m01, m02, m03,
      m10, m11, m12, m13,
      m20, m21, m22, m23,
      m30, m31, m32, m33
    )
  }
}
