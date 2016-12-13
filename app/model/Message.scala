package model

import java.util.Date

import play.api.libs.json.{JsValue, Json}

import scala.util.Random

class Message(val body:JsValue) {
  val id:String = Random.alphanumeric.take(10).mkString.toLowerCase
  val time:Long = System.currentTimeMillis()

  def info:JsValue = Json.obj("id" -> id, "time" -> time, "timeReadable" -> new Date(time).toString(), "message" -> body)
}
