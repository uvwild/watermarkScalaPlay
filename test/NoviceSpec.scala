import models.Document
import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test._

/**
 * to test out some scala details....
 * Created by uv on 05.04.2016 for watermarkScalaPlay
 */
@RunWith(classOf[JUnitRunner])
class NoviceSpec extends Specification {

  "test book and docs" in new WithApplication {
    val book = new Document(4711, "Robert Miller", "Gone With the Sand", "Fiction")
    book.id mustEqual 4711
  }


}
