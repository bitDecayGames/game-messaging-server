package controllers

import java.io.File
import javax.inject._

import db.ActiveGames
import model.Message
import play.api._
import play.api.libs.json.Json
import play.api.mvc._
import util.Unzip


@Singleton
class GameController @Inject() (conf: play.api.Configuration, env:Environment)  extends Controller {

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
    val game = if (env.mode != Mode.Prod) ActiveGames.registerNewGame(Option("AAAA")) else ActiveGames.registerNewGame()
    val gameFiles = conf.getString("gameFiles").getOrElse("public/tmp/")
    val file = new File(s"$gameFiles${game.id}.zip")
    println(s"Attempting to upload game files to ${file.getAbsolutePath}")
    if (file.exists) {
      println("File currently exists, deleting")
      file.delete
    }
    if (!file.getParentFile.exists) {
      println("Making parent folders")
      file.getParentFile.mkdirs
    }
    println("Save to file")
    request.body.moveTo(file)
    println("Unzip file")
    Unzip(s"$gameFiles${game.id}.zip", s"$gameFiles${game.id}")
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
