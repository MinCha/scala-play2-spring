package domain

import _root_.shared.IntegrationTest
import org.junit.Test
import org.springframework.beans.factory.annotation.Autowired

class MemoRepositoryTest extends IntegrationTest {
  @Autowired val sut: MemoRepository = null
  @Autowired val memoBookRepository: MemoBookRepository = null

  @Test def canSaveMemo() {
    val memoBook = someMemoBook
    memoBook.addMemo("Asap")
    memoBookRepository.save(memoBook)

    val memoId = memoBook.firstMemo.id
    assert(sut.exists(memoId))
  }
}
