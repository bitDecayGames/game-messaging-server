package model

import java.util.Date

import play.api.libs.json._

class Game(val id:String) {
  // TODO: this needs to be put back to random, just using AAAA for testing
  //def this()= this(Random.alphanumeric.take(4).mkString.toUpperCase)
  def this()= this("AAAA")

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