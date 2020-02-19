package modules

import com.google.inject.{AbstractModule, Provides}
import org.pac4j.core.client.Clients
import org.pac4j.oauth.client.TwitterClient
import org.pac4j.play.{CallbackController, LogoutController}
import play.api.{Configuration, Environment}
import java.nio.charset.StandardCharsets

import org.pac4j.play.store.{PlayCookieSessionStore, PlaySessionStore, ShiroAesDataEncrypter}
import org.pac4j.core.authorization.authorizer.RequireAnyRoleAuthorizer
import org.pac4j.core.config.Config
import org.pac4j.oauth.profile.twitter.TwitterProfile
import org.pac4j.play.http.PlayHttpActionAdapter
import org.pac4j.play.scala.{DefaultSecurityComponents, Pac4jScalaTemplateHelper, SecurityComponents}
/**
 * Guice DI module to be included in application.conf
 */
class SecurityModule(environment: Environment, configuration: Configuration) extends AbstractModule {

  val baseUrl = configuration.get[String]("baseUrl")

  override def configure(): Unit = {

    val sKey = configuration.get[String]("play.http.secret.key").substring(0, 16)
    val dataEncrypter = new ShiroAesDataEncrypter(sKey.getBytes(StandardCharsets.UTF_8))
    val playSessionStore = new PlayCookieSessionStore(dataEncrypter)
    bind(classOf[PlaySessionStore]).toInstance(playSessionStore)

    bind(classOf[SecurityComponents]).to(classOf[DefaultSecurityComponents])

    bind(classOf[Pac4jScalaTemplateHelper[TwitterProfile]])

    // callback
    val callbackController = new CallbackController()
    callbackController.setDefaultUrl("/")
    callbackController.setMultiProfile(false)
    bind(classOf[CallbackController]).toInstance(callbackController)

    // logout
    val logoutController = new LogoutController()
    logoutController.setDefaultUrl("/")
    bind(classOf[LogoutController]).toInstance(logoutController)
  }

  @Provides
  def provideTwitterClient: TwitterClient = new TwitterClient("HVSQGAw2XmiwcKOTvZFbQ", "FSiO9G9VRR4KCuksky0kgGuo8gAVndYymr4Nl7qc8AA")

  @Provides
  def provideConfig(twitterClient: TwitterClient): Config = {
    val clients = new Clients(baseUrl + "/callback", twitterClient)

    val config = new Config(clients)
    config.addAuthorizer("admin", new RequireAnyRoleAuthorizer[Nothing]("ROLE_ADMIN"))
    //config.addMatcher("excludedPath", new PathMatcher().excludeRegex("^/facebook/notprotected\\.html$"))
    config.setHttpActionAdapter(new PlayHttpActionAdapter)
    config
  }
}