package domain

import infrastructure.V
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemoBookService(
                       @Autowired repository: MemoBookRepository,
                       @Autowired v: V) {
  @Transactional
  def add(userId: String, memo: String): MemoBook = {
    val memoBook =
      if (repository.countByUserId(userId) == 1)
        repository.findByUserId(userId)
      else {
        new MemoBook(new User(userId, v.DefaultUserName))
      }

    memoBook.addMemo(memo)
    repository.save(memoBook)
    memoBook
  }

  def this() = this(null, null)
}
