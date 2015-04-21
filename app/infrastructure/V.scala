package infrastructure

import org.apache.commons.configuration.DatabaseConfiguration
import org.springframework.beans.factory.annotation.{Autowired, Value}
import org.springframework.cache.annotation.{CacheConfig, Cacheable}
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import play.Logger

import scala.util.Random

@CacheConfig(cacheManager = "cacheManager", cacheNames = Array("v"))
@Transactional
@Component
class V(@Autowired config: DatabaseConfiguration) {
  @Cacheable(key = "{#root.methodName}")
  def MaximumMemoPerMemoBook = get("MaximumMemoPerMemoBook")

  def DefaultUserName: String = "Unknown"

  @Value("${stage}")
  val Stage: String = ""

  def put(key: String, value: String) = {
    config.setProperty(key, value)
  }

  def get(key: String): String = {
    WatchTime("DBConfiguration -> " + key) {
      assertKey(key)
      val result = config.getString(key)
      Logger.info("Loaded Key : " + key + " -> " + result)
      result
    }
  }

  def getFloat(key: String): Float = {
    get(key).toFloat
  }

  def getInt(key: String): Int = {
    get(key).toInt
  }

  private def assertKey(key: String) = assert(config.containsKey(key))

  def this() = this(null)
}