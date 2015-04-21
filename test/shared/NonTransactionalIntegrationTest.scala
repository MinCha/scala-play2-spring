package shared

import javax.persistence.{EntityManager, PersistenceContext}
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitSuite
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.transaction.TransactionConfiguration

@TransactionConfiguration
@RunWith(classOf[SpringJUnit4ClassRunner])
@ContextConfiguration(Array("classpath:test/spring.xml"))
abstract class NonTransactionalIntegrationTest extends JUnitSuite with Fixture {
  @PersistenceContext val context: EntityManager = null

  def flush() {
    context.flush
  }

  def clear() {
    context.clear
  }

  def flushAndClear() {
    flush()
    clear()
  }
}
