package infrastructure.play2

import controllers.shared.BaseController
import org.junit.Test

class BaseControllerTest extends BaseController {

  @Test def canHandleNullWithoutImplicitChecking() {
    def mustBeNull: String = null
    val nonExistingStuff = mustBeNull

    assert(nonExistingStuff.optional.isEmpty)
  }

  @Test def canHandleNotNullWithoutImplicitChecking() {
    def mustBeNotNull: String = ""
    val existingStuff = mustBeNotNull

    assert(existingStuff.optional.isDefined)
  }
}
