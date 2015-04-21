package infrastructure.dbvariable

import _root_.shared.IntegrationTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class VariableRepositoryTest extends IntegrationTest {
  @Autowired val sut: VariableRepository = null

  @Test def canAddVariable() {
    val variable = new Variable("key", "value")

    sut.save(variable)

    val result = sut.findOne(variable.id)
    assert(result == variable)
  }
}
