package domain.exception

import domain.exception.ExceptionType.ShortReason

object ExceptionType extends Enumeration {
  type ShortReason = Int

  val InvalidStatus = 1
  val NotFound = 2
  val Unauthorized = 3
  val Forbidden = 4
  val Unregistered = 5
}

class BaseException(r: ShortReason, message: String) extends RuntimeException(message) {
  val shortReason = r
}

class NotFoundException(message: String, idz: String) extends BaseException(ExceptionType.NotFound, message) {
  val id = idz
}

class MemoNotFoundException(id: String) extends NotFoundException("Memo not found.", id)
