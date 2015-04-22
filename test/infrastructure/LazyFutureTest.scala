package infrastructure

import org.junit.Test
import play.api.Logger
import shared.UnitTest

class LazyFutureTest extends UnitTest {
  var number = 0

  @Test(expected = classOf[AssertionError])
  def mustNotRunOnOutOfLazyFutureExecutor() {
    LazyFuture {
      Logger.info("Must be not run.")
    }
  }

  @Test def LazyFutureBlockShouldBeRunAfterMainBlock() {
    LazyFutureBlockInCurrentThread {
      number += 1
      LazyFuture {
        number += 1
      }
    }

    assert(number == 2)
  }


  @Test def canExecuteBlockOnNestedLazyFuture() {
    LazyFutureBlockInCurrentThread {
      LazyFuture {
        number += 1
        LazyFuture {
          number += 1
        }
      }
      number += 1
    }

    assert(number == 3)
  }
}
