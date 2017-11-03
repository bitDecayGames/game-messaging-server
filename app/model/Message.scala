package model

import java.util.Date

import play.api.libs.json._

import scala.util.Random

class Message(val body:JsValue) {
  val id:String = Random.alphanumeric.take(10).mkString.toLowerCase
  val time:Long = System.currentTimeMillis()
  val messageType:Option[String] = (body \ "messageType").toOption match {
    case Some(str:JsString) => Option(str.value)
    case _ => None
  }

  def info:JsValue = Json.parse(toString)

  override def toString: String =
    s"""
      |{
      |  "id": "$id",
      |  "time": $time,
      |  "timeReadable": "${new Date(time).toString}",
      |  "message": ${Json.prettyPrint(body)}
      |}
    """.stripMargin
}

object Message{
  def apply(body:String):(Message, String) = {
    val msg = new Message(Json.parse(body))
    (msg, s"""{"id": "${msg.id}", "time": ${msg.time}, "timeReadable": "${new Date(msg.time).toString}", "message": $body }""")
  }
}
