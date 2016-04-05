package controllers

import models._

import play.api._
import play.api.mvc._
import play.api.libs.json._

object WatermarkScalaApplication extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def watermarkDocument = Action(BodyParsers.parse.json) { request =>
    val docResult = request.body.validate[Document]
    docResult.fold(
      errors => {
        BadRequest(Json.obj("status" -> "500", "message" -> JsError.toFlatJson(errors)))
      },
      document => {
        val ticket = Document.save(document)
        Ok(Json.toJson("banana"))
      }
    )
  }

  def todo() = play.mvc.Results.TODO

}
