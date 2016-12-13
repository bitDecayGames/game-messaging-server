package model

import java.util.Date

import play.api.libs.json.{JsValue, Json}

import scala.util.Random

class Game(val id:String) {
  def this()= this(Random.alphanumeric.take(4).mkString.toUpperCase)

  val initTime:Long = System.currentTimeMillis()
  private var messages:Seq[Message] = Seq()
  def lastUpdated:Long = messages.lastOption.map(_.time).getOrElse(initTime)

  def appendMessage(message:Message) = messages = messages ++ Seq(message)

  def getNewMessagesByIndex(inclusiveIndex:Int):Seq[Message] = if (inclusiveIndex < messages.size && inclusiveIndex >= 0) messages.slice(inclusiveIndex, messages.size) else Nil

  def getNewMessagesByTime(inclusiveTime:Long):Seq[Message] = messages.takeWhile(_.time >= inclusiveTime)

  def info:JsValue = Json.obj("id" -> id, "initTime" -> initTime, "initTimeReadable" -> new Date(initTime).toString(), "lastUpdated" -> lastUpdated, "lastUpdatedReadable" -> new Date(lastUpdated).toString(), "messages" -> messages.map(_.info))

  def simpleInfo:JsValue = Json.obj("id" -> id, "initTime" -> initTime, "initTimeReadable" -> new Date(initTime).toString(), "lastUpdated" -> lastUpdated, "lastUpdatedReadable" -> new Date(lastUpdated).toString(), "messageCount" -> messages.size)
}