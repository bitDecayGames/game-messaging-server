package controllers

import java.io._
import javax.inject.Inject

import play.api.Logger
import play.api.mvc._

class CustomExternalAssetsController @Inject() extends Controller {

  def at(rootPath: String, file: String) = Action { implicit request =>
    val fileToServe = new File(rootPath, file.replaceAll("\\.\\.", ""))
    if (fileToServe.exists) {
      Ok.sendFile(fileToServe, inline = true).withHeaders(CACHE_CONTROL -> "max-age=3600")
    } else {
      Logger.error(s"Could not find file: ${fileToServe.getAbsolutePath}")
      NotFound
    }
  }

}
