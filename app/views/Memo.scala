package views

import domain.{User, MemoBook, Memo}
import views.shared.View

class MemoView(memo: Memo) extends View {
  def getId = memo.id

  def getUserId = memo.userId

  def getMemo = memo.text

  def getCreated = memo.created
}

class MemoDetailView(memo: Memo) extends MemoView(memo) {
  def getUpdated = memo.updated

  def getMemoBook = new MemoBookView(memo.memoBook)

  def getUser = new UserView(memo.memoBook.user)
}

class MemoBookView(memoBook: MemoBook) extends View {
  def getId = memoBook.id

  def getCount = memoBook.count

  def getFirstMemo = new MemoView(memoBook.firstMemo)

  def getLastMemo = new MemoView(memoBook.lastMemo)
}

class UserView(user: User) extends View {
  def getId = user.id

  def getCreated = user.created

  def getUpdated = user.updated

  def getName = user.name
}