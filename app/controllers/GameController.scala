package controllers

import java.io.File
import javax.inject._

import db.ActiveGames
import model.Message
import play.api.libs.json.Json
import play.api.mvc._
import util.Unzip


@Singleton
class GameController @Inject() extends Controller {

  def games = Action { implicit request =>
    Ok(ActiveGames.info)
  }

  def getGame(id:String) = Action { implicit request =>
    ActiveGames.getGame(id).map(_.info) match {
      case Some(info) => Ok(info)
      case _ => NotFound(s"Could not find a game with the id: $id")
    }
  }

  def registerNewGame = Action(parse.temporaryFile){ request =>
    val game = ActiveGames.registerNewGame
    val file = new File(s"public/tmp/${game.id}.zip")
    if (file.exists) file.delete
    if (!file.getParentFile.exists) file.getParentFile.mkdirs
    request.body.moveTo(file)
    Unzip(s"public/tmp/${game.id}.zip", s"public/tmp/${game.id}")
    Ok(game.info)
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
      case Some(game) => Ok(Json.toJson(game.getNewMessagesByTime(time).map(_.info)))
      case _ => NotFound(s"Could not find a game with the id: $id")
    }
  }

  def getNewMessagesByIndex(id:String, indexStr:String) = Action { implicit request =>
    val index = indexStr.toInt
    ActiveGames.getGame(id) match {
      case Some(game) => Ok(Json.toJson(game.getNewMessagesByIndex(index).map(_.info)))
      case _ => NotFound(s"Could not find a game with the id: $id")
    }
  }

  def getLastMessageOfType(id:String, ofType:String) = Action { implicit request =>
    ActiveGames.getGame(id) match {
      case Some(game) => game.getLastMessageOfType(ofType) match {
        case Some(msg) => Ok(Json.toJson(msg.info))
        case _ => NotFound(s"Could not find any messages of type: $ofType")
      }
      case _ => NotFound(s"Could not find a game with the id: $id")
    }
  }
}
