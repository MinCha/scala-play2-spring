package infrastructure.play2

import play.api.Logger
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object AccessLogFilter extends Filter {
  val accessLogger = Logger("access")

  def apply(next: (RequestHeader) => Future[Result])(request: RequestHeader): Future[Result] = {
    val startTime = System.currentTimeMillis
    val resultFuture = next(request)
    resultFuture.foreach(result => {
      val requestTime = System.currentTimeMillis - startTime
      val msg = s"method=${request.method} uri=${request.uri} remote-address=${request.remoteAddress}" + s" response time=${requestTime}ms"
      s" status=${result.header.status}";
      accessLogger.info(msg)
    })

    resultFuture
  }
}
