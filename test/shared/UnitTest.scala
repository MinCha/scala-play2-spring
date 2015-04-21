package shared

import java.util.Calendar

import org.joda.time.DateTime
import org.scalatest.junit.JUnitSuite
import org.scalatest.mock.MockitoSugar
import play.Logger

abstract class UnitTest extends JUnitSuite with Fixture with MockitoSugar {
  def fixedTime = {
    val result = Calendar.getInstance()
    result.setTimeInMillis(0)
    result.getTime
  }

  def fixedDateTime = {
    new DateTime(0)
  }

  def waitFor(miliseconds: Int) {
    Thread.sleep(miliseconds)
  }

  def pass() = {
    Logger.info("Passed")
  }
}
