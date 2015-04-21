package infrastructure

import play.api.Logger

class WatchTime {

  def measure[T](name: String)(f: => T): T = {
    val current = System.currentTimeMillis
    val result = f
    Logger.info("%s Consumed time -> %s ms"
      .format(name, (System.currentTimeMillis - current).toString))
    result
  }
}

object WatchTime {
  def apply[T](name: String)(f: => T): T = new WatchTime().measure(name)(f)
}
