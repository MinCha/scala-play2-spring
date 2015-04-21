package infrastructure

import org.junit.Test
import shared.UnitTest

class RetryTest extends UnitTest {

  @Test def shouldTryRepeatlyUntilRetryCount() {
    val obj = new SuccessAfterTwoCalls

    val result = Retry(3) {
      obj.execute
    }

    assert(result == 3)
  }

  @Test def shouldNotTryNoMoreWhenAlreadySuccess() {
    val result = Retry(3) {
      1
    }

    assert(result == 1)
  }

  @Test(expected = classOf[RetryExcessException])
  def shouldThrowExceptionWhenExceedingRetryCount() {
    val obj = new SuccessAfterTwoCalls

    val result = Retry(2) {
      obj.execute
    }

    assert(result == 1)
  }

  class SuccessAfterTwoCalls {
    var count = 0

    def execute(): Int = {
      count = count + 1
      if (count < 3) throw new IllegalStateException else count
    }
  }

}
