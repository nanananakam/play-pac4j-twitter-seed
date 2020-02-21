package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._

/**
 * Add your spec here.
 * You can mock out a whole application including requests, plugins etc.
 *
 * For more information, see https://www.playframework.com/documentation/latest/ScalaTestingWithScalaTest
 */
class MyPageControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "MyPageController GET" should {

    "render the index page from the router" in {
      val request = FakeRequest(GET, "/mypage")
      val response = route(app, request).get

      status(response) mustBe FOUND
      redirectLocation(response).head must include("twitter.com")
    }
  }
}

