import models._
import org.specs2.mutable._
import org.specs2.runner._
import org.junit.runner._
import play.api.libs.json.Json

import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 * For more information, consult the wiki.
 */
@RunWith(classOf[JUnitRunner])
class ApplicationSpec extends Specification {

  "Application" should {

    "send 404 on a bad request" in new WithApplication {
      route(FakeRequest(GET, "/boum")) must beNone
    }

    "render the index page" in new WithApplication {
      val home = route(FakeRequest(GET, "/")).get

      status(home) must equalTo(OK)
      contentType(home) must beSome.which(_ == "text/html")
      contentAsString(home) must contain("Your new application is ready.")
    }

    "call watermark entry" in new WithApplication {
      val testbook = Document.getTestBook1
      val fakeRequest = FakeRequest(
        POST,
        controllers.routes.WatermarkScalaApplication.watermarkDocument().url,
        FakeHeaders(Seq("Accept" -> Seq("application/json"))),
        Json.toJson(testbook)
      )
      val Some(result) = route(fakeRequest)
      // status method contains the Await...
      val futureStatus = status(result)
      if (futureStatus != CREATED) println(contentAsString(result))      // debugging for newbies
      futureStatus must equalTo(CREATED)

      contentType(result) must equalTo(Some("application/json"))
      contentAsJson(result) must equalTo(Json.toJson(Ticket.getTestTicketFor(testbook)))

      val fakeRequest2 = FakeRequest(
        POST,
        controllers.routes.WatermarkScalaApplication.watermarkDocument().url,
        FakeHeaders(Seq("Accept" -> Seq("application/json"))),
        Json.toJson(testbook)
      )
      val Some(result2) = route(fakeRequest2)
      // status method contains the Await...
      val futureStatus2 = status(result2)
      if (futureStatus2 != ACCEPTED) println(contentAsString(result2))      // debugging for newbies
      futureStatus2 must equalTo(ACCEPTED)
      contentType(result2) must equalTo(Some("application/json"))
      contentAsJson(result2) must equalTo(Json.toJson(Ticket.getTestTicketFor(testbook)))

    }
  }
}
