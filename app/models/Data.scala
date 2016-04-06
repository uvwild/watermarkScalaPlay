package models

/**
 * Created by uv on 05.04.2016 for watermarkScalaPlay
 * Using a Simplified Data Model where the ticket id is identical to the document id
 */

import java.util.concurrent.ConcurrentHashMap

import play.api.libs.json._
import play.api.libs.functional.syntax._


// no inheritance due to json mapping constraints in scala
// use Option wrapper for optional arguments to make json mapping work
// http://stackoverflow.com/questions/4199393/are-options-and-named-default-arguments-like-oil-and-water-in-a-scala-api
// TODO this option wrapping is not the best solution....
case class Document(id: Int, author: String, title: String, topic : Option[String] = None, watermark: Option[String] = None)

// TODO missing validation for the topics
// TODO still lacking proper modelling for the subtypes Book Journal
object Document {

  // built in simple doc storage
  private val concurrentHashMap = new ConcurrentHashMap[Long, Document]()

  // json read write
  implicit val reader: Reads[Document] = (
    (JsPath \ "id").read[Int] and
     (JsPath \ "author").read[String] and
     (JsPath \ "title").read[String] and
     (JsPath \ "topic").readNullable[String] and
     (JsPath \ "watermark").readNullable[String]      // readNullable requires Option wrapper on field
    )(Document.apply _)

  implicit val writer: Writes[Document] = (
    (JsPath \ "id").write[Int] and
    (JsPath \ "author").write[String] and
    (JsPath \ "title").write[String] and
    (JsPath \ "topic").writeNullable[String] and    // writeNullable  requires Option wrapper on field too
    (JsPath \ "watermark").writeNullable[String]
    ) (unlift(Document.unapply))

  // testdata
  def getTestBook1: Document = {
    return new Document(4711, "Margaret Mitchell", "Gone With the Sand", Option("Fiction"))
  }

  def getTestBook2: Document = {
    return new Document(9999, "Aldous Huxley", "Brave Old World", Option("Fiction"))
  }

  def getTestJournal1: Document = {
    return new Document(123, "Humphrey Bogart", "Alcantara")
  }

  // built-in storage ;)
  def save(document: Document): Ticket = {
    val previous = concurrentHashMap.putIfAbsent(document.id, document)
    return if (previous == null) {
      val ticket = new Ticket(document.id, document)
      return ticket
    } else
      null
  }

  def get(id: Int): Document = {
    return concurrentHashMap.get(id)
  }
}

case class Ticket(id: Int, document: Document)

object Ticket {
  // json read write
  implicit val reader: Reads[Ticket] = (
    (__ \ "id").read[Int] and
     (__ \ "document").read[Document]
    )(Ticket.apply _)

  implicit val writer: Writes[Ticket] = (
      (JsPath \ "id").write[Int] and
      (JsPath \ "document").write[Document]
    ) (unlift(Ticket.unapply))


  // create test ticket for given doc = custom contructor
  def getTestTicketFor(document: Document): Ticket = {
    return new Ticket(document.id,document)
  }

}

