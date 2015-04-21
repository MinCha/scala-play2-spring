package views.shared

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility
import com.fasterxml.jackson.annotation.JsonInclude.Include
import com.fasterxml.jackson.annotation.{JsonAutoDetect, JsonInclude, JsonPropertyOrder}
import infrastructure.ErrorDescription

@JsonPropertyOrder(alphabetic = true)
@JsonInclude(Include.NON_NULL)
@JsonAutoDetect(fieldVisibility = Visibility.NONE, getterVisibility = Visibility.PUBLIC_ONLY, setterVisibility =
  Visibility.NONE)
trait View

class ResultStatus(success: Boolean, code: Int, message: Option[String] = None) extends View {
  def getSuccess = success

  def getCode = code

  def getMessage = message
}

class SuccessResult(success: Boolean, detail: Any) extends View {
  def getResult = new ResultStatus(success, 0)

  def getContents = detail
}

class FailureResult(description: ErrorDescription) extends View {
  def getResult = new ResultStatus(false, description.errorType, Option(description.message))
}

class ListView(hasNext: Boolean, list: List[View]) extends View {
  def getHasNext = hasNext

  def getSize = list.size

  def getItems = list
}