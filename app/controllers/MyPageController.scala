package controllers

import play.api.mvc._
import org.pac4j.play.scala._
import javax.inject.Inject
import org.pac4j.oauth.profile.twitter.TwitterProfile

import scala.collection.JavaConverters._

class MyPageController @Inject() (val controllerComponents: SecurityComponents, implicit val pac4jTemplateHelper: Pac4jScalaTemplateHelper[TwitterProfile]) extends Security[TwitterProfile] {

  def index = Secure("TwitterClient") { implicit request =>
    Ok(views.html.mypage(profiles))
  }

}

