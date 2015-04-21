package infrastructure.play2

import com.typesafe.config.ConfigFactory
import domain.exception.NotFoundException
import infrastructure.{ErrorDescription, Json, V}
import org.springframework.cache.ehcache.EhCacheCacheManager
import org.springframework.context.support.ClassPathXmlApplicationContext
import play.api.mvc._
import play.api.{Application, GlobalSettings, Logger}
import views.shared.FailureResult

import scala.collection.JavaConversions._
import scala.concurrent.Future

object Global extends WithFilters(AccessLogFilter) with GlobalSettings with Results {
  var ctx: ClassPathXmlApplicationContext = _
  val LineSeparatorKey: String = "line.separator"

  override def onStart(app: Application) {
    Logger.info("Loading Spring Context")

    ctx = new ClassPathXmlApplicationContext(ConfigFactory.load().getString("spring.context.location"))
    ctx.start()
    Logger.info("Bean count : " + ctx.getBeanDefinitionCount)
    ctx.getBeanDefinitionNames.foreach(x => Logger.info("Bean -> '" + x) + "'")

    val env = ctx.getBean(classOf[V])
    Logger.info("Current env: " + env.Stage)

    val cacheManager = ctx.getBean(classOf[EhCacheCacheManager])
    cacheManager.getCacheNames.foreach(x => Logger.info("Cache: " + x))
  }

  override def getControllerInstance[A](controllerClass: Class[A]): A = {
    ctx.getBean(controllerClass)
  }

  override def onStop(app: Application) {
    ctx.close()
    super.onStop(app)
  }

  override def onRequestReceived(request: RequestHeader): (RequestHeader, Handler) = {
    Logger.info("Header on request -> " + headerAsString(request))
    super.onRequestReceived(request)
  }

  override def onError(request: RequestHeader, ex: Throwable): Future[Result] = {
    val newline = sys.props(LineSeparatorKey)
    val tab = "\t"
    val e = ex.getCause()

    Logger.error("Header on error -> " + headerAsString(request))

    val message = e.getClass.getSimpleName + " " + e.getMessage
    e match {
      case e: NotFoundException =>
        Future.successful(NotFound(Json.toJson(new FailureResult(ErrorDescription(e.shortReason, message, e.id)))))

      case _ => Future.successful(InternalServerError(Json.toJson(new FailureResult(ErrorDescription(Integer.MAX_VALUE, message)))))
    }
  }

  private def headerAsString(request: RequestHeader): String = {
    if (request.headers == null) ""
    else request.headers.toMap.map(x => "%s:%s;".format(x._1, x._2)).mkString.replaceAll("ArrayBuffer", "")
  }
}
