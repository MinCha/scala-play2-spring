package infrastructure

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.{Propagation, Transactional}
import shared.NonTransactionalIntegrationTest

class InTransactionTest extends NonTransactionalIntegrationTest {
  @Autowired
  var sut: InTransaction = null
  @Autowired
  var mustBeInTransaction: MustBeInTransaction = null

  @Test def mustBeRunInTransactionalScope() {
    assert(1 == sut.execute {
      mustBeInTransaction.returnOne
    })
  }
}

@Component
class MustBeInTransaction {
  @Transactional(propagation = Propagation.MANDATORY) def returnOne = 1
}
