package infrastructure

import java.net.HttpRetryException

import play.api.Logger

/**
 * Created by vayne on 15. 2. 12..
 */
class Retry {
  def execute[T](count: Int)(f: => T): T = {
    try {
      if (count > 0) f else throw new RetryExcessException
    } catch {
      case e: RetryExcessException => throw e
      case e: Throwable => {
        Logger.error(e.toString)
        execute(count - 1)(f)
      }
    }
  }
}

class RetryExcessException extends RuntimeException

object Retry {
  def apply[T](count: Int)(f: => T): T = new Retry().execute(count)(f)

  def ->[T](count: Int)(f: => T): T = apply(count)(f)
}

object Retry3 {
  def apply[T](f: => T): T = new Retry().execute(3)(f)

  def ->[T](f: => T): T = apply(f)
}

object Retry5 {
  def apply[T](f: => T): T = new Retry().execute(5)(f)

  def ->[T](f: => T): T = apply(f)
}