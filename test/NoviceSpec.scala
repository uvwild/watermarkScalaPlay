import models.Document
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.libs.json.Json
import play.api.test._

/**
 * to test out some scala details....
 * Created by uv on 05.04.2016 for watermarkScalaPlay
 */
@RunWith(classOf[JUnitRunner])
class NoviceSpec extends Specification {

  "test (test) - docs ctor" in new WithApplication {
    val doc = new Document(4711, "Robert Miller", "Gone With the Sand", "Fiction")
    doc.id mustEqual 4711
  }

  "testJsonConversion" in new WithApplication() {
    val doc1 = Document.getTestDoc1
    val doc2 = Document.getTestDoc2
    val json1 = Json.toJson(doc1)
    val json2 = Json.toJson(doc2)
    json1 mustNotEqual json2

    // and back from json
    val doc1json = Json.fromJson[Document](json1).get
    val doc2json = Json.fromJson[Document](json2).get

    doc1json mustEqual doc1
    doc2json mustEqual doc2
  }
}
