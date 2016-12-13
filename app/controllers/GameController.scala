package controllers

import javax.inject._

import db.ActiveGames
import model.Message
import play.api._
import play.api.libs.json.Json
import play.api.mvc._

@Singleton
class GameController @Inject() extends Controller {

  def index = Action { implicit request =>
    Ok(ActiveGames.info)
  }

  def registerNewGame = Action { implicit request =>
    Ok(ActiveGames.registerNewGame.info)
  }

  def getGame(id:String) = Action { implicit request =>
    ActiveGames.getGame(id).map(_.info) match {
      case Some(info) => Ok(info)
      case _ => NotFound(s"Could not find a game with the id: $id")
    }
  }

  def appendMessage(id:String) = Action { implicit request =>
    ActiveGames.getGame(id) match {
      case Some(game) =>
        request.body.asJson match {
          case Some(jsonBody) =>
            val message = new Message(jsonBody)
            game.appendMessage(message)
            Ok("")
          case _ => BadRequest("Message must be in a Json format")
        }
      case _ => NotFound(s"Could not find a game with the id: $id")
    }
  }

  def getNewMessagesByTime(id:String, timeStr:String) = Action { implicit request =>
    val time = timeStr.toLong
    ActiveGames.getGame(id) match {
      case Some(game) => Ok(Json.arr(game.getNewMessagesByTime(time).map(_.info)))
      case _ => NotFound(s"Could not find a game with the id: $id")
    }
  }

  def getNewMessagesByIndex(id:String, indexStr:String) = Action { implicit request =>
    val index = indexStr.toInt
    ActiveGames.getGame(id) match {
      case Some(game) => Ok(Json.arr(game.getNewMessagesByIndex(index).map(_.info)))
      case _ => NotFound(s"Could not find a game with the id: $id")
    }
  }
}