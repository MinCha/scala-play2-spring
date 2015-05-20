package domain

import infrastructure.V
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MemoBookService(
                       @Autowired userRepository: UserRepository,
                       @Autowired memoBookRepository: MemoBookRepository,
                       @Autowired v: V) {
  @Transactional
  def add(userId: String, memo: String): MemoBook = {
    val user = userRepository.findOne(userId)

    val memoBook =
      if (memoBookRepository.countByUserId(userId) == 1)
        memoBookRepository.findByUserId(userId)
      else {
        new MemoBook(user)
      }

    memoBook.addMemo(memo)
    memoBookRepository.save(memoBook)
    memoBook
  }

  def this() = this(null, null, null)
}
