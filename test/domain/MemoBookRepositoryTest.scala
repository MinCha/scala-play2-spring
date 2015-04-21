package domain

import _root_.shared.IntegrationTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class MemoBookRepositoryTest extends IntegrationTest {
  @Autowired val sut: MemoBookRepository = null
  @Autowired val userRepository: UserRepository = null

  @Test def canCreateNotebook() {
    val memoBook = new MemoBook(someUser)

    sut.save(memoBook)
    flushAndClear()

    val result = sut.findOne(memoBook.id)
    assert(result.id == memoBook.id)
  }

  @Test def canAddMemo() {
    val memoBook = new MemoBook(someUser)
    val firstMemo = "Today is rainy day."
    val secondMemo = "Please contact to me."

    memoBook.addMemo(firstMemo)
    memoBook.addMemo(secondMemo)

    sut.save(memoBook)
    flushAndClear()
    val result = sut.findOne(memoBook.id)
    assert(result.count == 2)
    assert(result.firstMemo.text.contains("rainy day"))
    assert(result.lastMemo.text.contains("contact to me"))
  }

  @Test def canKnowWhetherExistingMemoBookOrNot() {
    val user = someUser
    sut.save(new MemoBook(user))

    assert(sut.countByUserId(user.id) == 1)
  }
}
