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
        val ticket = Document.save(document)      // ticket is null if document has been posted before
        // use different status code if doc has been found  TODO this can be wrong when called with differnt document versions!!)
        if (ticket != null) Created(Json.toJson(ticket)).withHeaders("Content-Type" -> "application/json")
        else                Accepted(Json.toJson(Ticket.getTestTicketFor(document))).withHeaders("Content-Type" -> "application/json")
      }
    )
  }

  def todo() = play.mvc.Results.TODO

}
