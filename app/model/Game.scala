package model

import java.util.Date

import play.api.libs.json._

import scala.util.Random

class Game(val id:String) {
  def this() = this(Game.randomId())

  val initTime:Long = System.currentTimeMillis()
  private var messages:Seq[Message] = Seq()
  def lastUpdated:Long = messages.lastOption.map(_.time).getOrElse(initTime)

  def appendMessage(message:Message) = messages = messages ++ Seq(message)

  def getNewMessagesByIndex(inclusiveIndex:Int):Seq[Message] = if (inclusiveIndex < messages.size && inclusiveIndex >= 0) messages.slice(inclusiveIndex, messages.size) else Nil

  def getNewMessagesByTime(inclusiveTime:Long):Seq[Message] = messages.dropWhile(_.time < inclusiveTime)

  def getLastMessageOfType(ofType:String):Option[Message] = messages.filter(_.messageType.contains(ofType)) match {
    case Nil => None
    case msgs => Option(msgs.maxBy(_.time))
  }

  def info:JsValue = Json.obj("id" -> id, "initTime" -> initTime, "initTimeReadable" -> new Date(initTime).toString(), "lastUpdated" -> lastUpdated, "lastUpdatedReadable" -> new Date(lastUpdated).toString(), "messages" -> messages.map(_.info))

  def simpleInfo:JsValue = Json.obj("id" -> id, "initTime" -> initTime, "initTimeReadable" -> new Date(initTime).toString(), "lastUpdated" -> lastUpdated, "lastUpdatedReadable" -> new Date(lastUpdated).toString(), "messageCount" -> messages.size)
}

object Game{
  val hexChars = Seq("1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "a", "b", "c", "d", "e", "f")
  def randomId():String = (1 to 4).map(_ => hexChars(Random.nextInt(hexChars.size))).mkString.toUpperCase
}