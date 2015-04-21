package domain

import _root_.shared.IntegrationTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class UserRepositoryTest extends IntegrationTest {
  @Autowired val sut: UserRepository = null

  @Test def canAddUser() {
    val user = new User("vayne.q", "vayne")

    sut.save(user)
    flushAndClear()

    val result = sut.findOne(user.id)
    assert(result.id == user.id)
  }
}
