package infrastructure.play2

import org.junit.Test
import play.api.mvc.RequestHeader
import play.mvc.Http.Status
import shared.UnitTest
import play.api.libs.concurrent.Execution.Implicits._

import scala.util.{Failure, Success}

class GlobalTest extends UnitTest {
  val request = mock[RequestHeader]

  @Test def shouldReturnInternalServerErrorWhenOccuringUnexpectedException() {
    val exception = new RuntimeException(new NullPointerException)

    Global.onError(request, exception).onComplete {
      case Success(x) => assert(x.header.status == Status.INTERNAL_SERVER_ERROR)
      case Failure(x) => fail
    }
  }
}