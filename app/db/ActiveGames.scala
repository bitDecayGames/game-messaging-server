package db

import model.Game
import play.api.libs.json.{JsValue, Json}

object ActiveGames {
  private var games:Seq[Game] = Seq()

  def registerNewGame:Game = {
    val game = new Game()
    registerNewGame(game)
  }

  def registerNewGame(game:Game):Game = {
    games = games.filter(curGame => curGame.id != game.id) ++ Seq(game)
    game
  }

  def getGame(id:String):Option[Game] = games.find(_.id.equalsIgnoreCase(id.trim))

  def count:Int = games.size
  def size:Int = count

  def info:JsValue = Json.obj("games" -> games.map(_.simpleInfo), "count" -> count)
}
