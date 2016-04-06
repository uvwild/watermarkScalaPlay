import models._
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.libs.json.{JsSuccess, Json}
import play.api.test._

/**
 * to test out some scala details....
 * Created by uv on 05.04.2016 for watermarkScalaPlay
 */
@RunWith(classOf[JUnitRunner])
class NoviceSpec extends Specification {

  "can use ctor" in new WithApplication {
    val doc = Document(id = 4711, author = "Robert Miller", title = "Gone With the Sand", topic = Option("Fiction"))
    doc.id mustEqual 4711
    doc.author mustEqual "Robert Miller"
    doc.watermark mustEqual None
  }

  "convert data to json" in new WithApplication() {
    val doc1 = Document.getTestBook1
    val doc2 = Document.getTestBook2
    val json1 = Json.toJson(doc1)
    val json2 = Json.toJson(doc2)
    json1 mustNotEqual json2

    // and back from json
    val doc1json = Json.fromJson[Document](json1).get
    val doc2json = Json.fromJson[Document](json2).get

    doc1json mustEqual doc1
    doc2json mustEqual doc2

    // tickets
    val ticket = Ticket.getTestTicketFor(Document.getTestBook1)
    val tkt2json = Json.toJson(ticket)
    val tktFromJson = Json.fromJson[Ticket](tkt2json).get
    tktFromJson mustEqual ticket
  }
}
