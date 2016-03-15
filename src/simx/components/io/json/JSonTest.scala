package simx.components.io.json

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

import org.json4s.jackson.Serialization
import simplex3d.math.float._
import simx.components.io.json.settings.JacksonSettings
import simx.core.entity.Entity
import akka.actor.{Props, ActorSystem}
import simx.core.svaractor.unifiedaccess.EntityUpdateHandling
import simx.components.io.json.typehints._
import simx.core.svaractor.SVarActor

case class Test(semantics : Map[String, Any])
/**
 * Created by dwiebusch on 12.05.14
 */
case class UnityEntity(id : java.util.UUID, mat : ConstMat4)

object JSonTest {
  class TestActor extends SVarActor with EntityUpdateHandling{

    JacksonSettings.init()
    override protected def removeFromLocalRep(e: Entity){}

    /**
     * called when the actor is started
     */


    override protected def startUp(){
      StackableHint.registerOverride(scala.reflect.classTag[Test], "T")
//      implicit val hint = TypeHints.shortHints(classOf[Test]:: classOf[String] :: Nil, ConstMat4fHint, UUIDHint)
      implicit val hint = TypeHints.shortHints(classOf[Test] :: Nil, UUIDHint, ConstVec3fHint, ConstQuat4fHint, StringHint)
      println(Serialization.write(Test(Map("x" -> ConstQuat4(1,0,0,1)))))
      println(Serialization.read(Serialization.write(Test(Map("x" -> ConstQuat4(1,0,0,1))))))
//      println(Serialization.read("""{"jsonClass":"Test","semantics":{"size":{"jsonClass":"String", "value":"test"}, "size2":{"jsonClass":"ConstVec3f","x":4.5,"y":0.5,"z":4.5}}}"""))
    }
  }

  def main (args: Array[String]) {
    val system = ActorSystem.apply("test")
    system.actorOf(Props[TestActor]())


  }
}
