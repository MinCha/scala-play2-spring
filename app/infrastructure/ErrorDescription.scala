package infrastructure

import domain.exception.ExceptionType.ShortReason

case class ErrorDescription(errorType: ShortReason, message: String, detail: Any = "")