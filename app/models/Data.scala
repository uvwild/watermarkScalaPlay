package models

/**
 * Created by uv on 05.04.2016 for watermarkScalaPlay
 */

import java.util.concurrent.ConcurrentHashMap

import play.api.libs.json._
import play.api.libs.functional.syntax._


// no inheritance due to json mapping constraints in scala
case class Document(id: Int, author: String, title: String, topic: String = null, watermark: String = null)

// TODO missing validation for the topics
// TODO lacking proper modelling for the subtypes Book Journal
object Document {
  // (id: Int, author: String, title: String, watermark: String = null)
  implicit val reader: Reads[Document] = (
    (JsPath \ "id").read[Int] and
     (JsPath \ "author").read[String] and
     (JsPath \ "title").read[String] and
     (JsPath \ "topic").read[String] and
     (JsPath \ "watermark").read[String]
    )(Document.apply _)

  implicit val writer: Writes[Document] = (
    (JsPath \ "id").write[Int] and
    (JsPath \ "author").write[String] and
    (JsPath \ "title").write[String] and
    (JsPath \ "topic").write[String]  and
    (JsPath \ "watermark").write[String]
    ) (unlift(Document.unapply))

  def getTestDoc1: Document = {
    return new Document(4711, "Margaret Mitchell", "Gone With the Sand", "Fiction")
  }

  def getTestDoc2: Document = {
    return new Document(9999, "Aldous Huxley", "Brave Old World", "Fiction")
  }

  private val concurrentHashMap = new ConcurrentHashMap[Long, Document]()

  def save(document: Document): Ticket = {
    val previous = concurrentHashMap.putIfAbsent(document.id, document)
    if (previous == null) {
      val ticket = new Ticket(document.id, document)
      return ticket
    } else
      return null
  }

  def get(id: Int): Document = {
    return concurrentHashMap.get(id)
  }
}

case class Ticket(id: Int, document: Document)

object Ticket {
  implicit val reader: Reads[Ticket] = (
    (__ \ "id").read[Int] and
     (__ \ "document").read[Document]
    )(Ticket.apply _)
}

