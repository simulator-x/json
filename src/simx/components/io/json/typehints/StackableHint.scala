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

import org.json4s
import org.json4s._

import simplex3d.math.float.{ConstQuat4, ConstVec3}

import scala.language.implicitConversions
import scala.reflect.{classTag, ClassTag}



/**
 * Created by dwiebusch on 13.05.14
 */

object TypeHints{
  def jsonClass : String = "jc"

  def shortHints(classes : List[Class[_]], more : StackableHint[_]*) = {
    new TypeHints(more.toList, classes).asShortHint
  }

  def shortHints(more : StackableHint[_]*) = {
    new TypeHints(more.toList, Nil).asShortHint
  }

  def fullHints(classes : List[Class[_]], more : StackableHint[_]*) = {
    new TypeHints(more.toList, classes).asFullHint
  }

  def fullHints(more : StackableHint[_]*) = {
    new TypeHints(more.toList, Nil).asFullHint
  }
}


protected class TypeHints(hs : List[StackableHint[_]], classes : List[Class[_]]){
  final def asShortHint  =
    createFormat(shortHint)

  final def asFullHint  =
    createFormat(fullHint)

  private def createFormat(_typeHints : json4s.TypeHints) : Formats = new Formats {
    override def dateFormat: DateFormat = DefaultFormats.lossless.dateFormat
    override val typeHints: json4s.TypeHints = _typeHints
    override val typeHintFieldName: String = TypeHints.jsonClass
  }

  private val shortHint = new ShortTypeHints(hs.map(_.clazz) ::: classes) {
    private val clsMap =
      hs.map(x => x.clazz -> x.shortName).
        union(classes.map(x => x -> StackableHint.lookup(x).getOrElse(super.hintFor(x)))).
        toMap[Class[_], String]

    override def hintFor(clazz: Class[_]): String = clsMap.getOrElse(clazz, super.hintFor(clazz))
    override val deserialize = hs.foldRight(super.deserialize){  _._deserialize(full = false) orElse _}
    override val serialize   = hs.foldRight(super.serialize ){ _._serialize orElse _ }
  }

  private val fullHint = new FullTypeHints(hs.map(_.clazz) ::: classes) {
    private val clsMap =
      hs.map(x => x.clazz -> x.fullName).
        union(classes.map(x => x -> StackableHint.lookup(x).getOrElse(super.hintFor(x)))).
        toMap[Class[_], String]

    override def hintFor(clazz: Class[_]): String = clsMap.getOrElse(clazz, super.hintFor(clazz))
    override val deserialize = hs.foldRight(super.deserialize){  _._deserialize(full = true) orElse _}
    override val serialize   = hs.foldRight(super.serialize ){ _._serialize orElse _ }
  }
}


object StackableHint{
  private var map = Map[String, ClassTag[_]](
    "v3" -> classTag[ConstVec3],
    "q4" -> classTag[ConstQuat4]
  )

  def lookup(c : Class[_]) : Option[String] =
    map.find(_._2.runtimeClass == c).map(_._1)

  def registerOverride(classTag : ClassTag[_], name : String): Unit = synchronized{
    if (map.get(name).collect{ case clt => clt != classTag }.getOrElse(false) )
      throw new Exception("ERROR: " + name + " was registered for " + map(name) + " before, this will yield ambiguity")
    else if (!map.contains(name)) {
      map = map + (name -> classTag)
      println("current registrycontent:\n\t" + map.map(x => x._1 + " for " + x._2 ).mkString("\n\t"))
    }
  }
}

abstract class StackableHint[T : ClassTag](val overrideClassName : Option[String] = None){
  protected val deserialize : PartialFunction[JObject, T]
  protected val serialize : PartialFunction[T, JObject]

  def this(overrideClassName : String) =
    this(Some(overrideClassName))

  private[typehints] final val clazz : Class[_] = scala.reflect.classTag[T].runtimeClass
  private[typehints] final val shortName = overrideClassName.getOrElse(clazz.getSimpleName)
  private[typehints] final val fullName = clazz.getCanonicalName

  private final val deSerShort : PartialFunction[(String, JObject), Any] = {
    case (`shortName`, x) if deserialize.isDefinedAt(x) => deserialize(x)
  }

  private final val deSerFull : PartialFunction[(String, JObject), Any] = {
    case (`fullName`, x) if deserialize.isDefinedAt(x) => deserialize(x)
  }

  private[typehints] final val _serialize : PartialFunction[Any, JObject] = {
    case x : T if serialize.isDefinedAt(x) => serialize(x)
  }

  private[typehints] def _deserialize(full : Boolean) =
    if (full) deSerFull else deSerShort

  StackableHint.registerOverride(implicitly[ClassTag[T]], shortName)
}


case class JSonFloat(value: Float)