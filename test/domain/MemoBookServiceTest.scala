package domain

import _root_.shared.UnitTest
import infrastructure.V
import org.junit.Test
import org.mockito.Matchers._
import org.mockito.Mockito._

class MemoBookServiceTest extends UnitTest {
  val memoBookRepository = mock[MemoBookRepository]
  val userRepository = mock[UserRepository]
  val v = mock[V]
  val memoBook = mock[MemoBook]
  val user = mock[User]

  @Test def shouldUseExistingMemoBook() {
    val sut = new MemoBookService(userRepository, memoBookRepository, v)
    val userId = "vayne"
    val memo = "I am very happy."
    when(userRepository.findOne(userId)).thenReturn(user)
    when(memoBookRepository.countByUserId(userId)).thenReturn(1)
    when(memoBookRepository.findByUserId(userId)).thenReturn(memoBook)

    sut.add(userId, memo)

    verify(memoBookRepository).findByUserId(userId)
  }

  @Test def shouldCreateMemoBookWhenNoMemoBookYet() {
    val sut = new MemoBookService(userRepository, memoBookRepository, v)
    val userId = "vayne"
    val memo = "I am very happy."
    when(userRepository.findOne(userId)).thenReturn(user)
    when(memoBookRepository.countByUserId(userId)).thenReturn(0)

    sut.add(userId, memo)

    verify(memoBookRepository).countByUserId(userId)
    verify(memoBookRepository).save(any[MemoBook])
    verifyNoMoreInteractions(memoBookRepository)
  }
}