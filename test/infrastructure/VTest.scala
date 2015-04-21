package infrastructure

import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired
import shared.IntegrationTest

class VTest extends IntegrationTest {
  @Autowired val sut: V = null

  @Test def canReadVariableFromDatabase {
    val key = "keyA"
    sut.put(key, "1.0")

    assert(sut.get(key) == "1.0")
    assert(sut.getFloat(key) == 1.0f)
  }

  @Test def canReadVariableFromEnvironmentFile {
    assert(sut.Stage == "test")
  }

  @Test def shouldEnsureLastValueInNearRealTime {
    val key = "keyA"
    sut.put(key, "1.0")

    sut.put(key, "2.0")
    assert(sut.getFloat(key) == 2.0f)
  }

  @Test(expected = classOf[AssertionError]) def throwExceptionWhenKeyDoesNotExist {
    sut.get("nonExistingKey")
  }
}