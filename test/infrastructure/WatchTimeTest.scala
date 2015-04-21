package infrastructure

import org.junit.Test
import shared.UnitTest

class WatchTimeTest extends UnitTest {
  @Test def mustRunCodeInBlock() {
    var total = 1
    WatchTime("UnitTest") {
      total = total + 1
    }

    assert(total == 2)
  }
}
