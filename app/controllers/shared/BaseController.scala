package controllers.shared

import infrastructure.{ErrorDescription, Json, V}
import org.springframework.beans.factory.annotation.Autowired
import play.api.data.Form
import play.api.mvc.{Result, Controller}
import views.shared.{ListView, View, FailureResult, SuccessResult}

abstract class BaseController extends Controller {
  @Autowired
  val v: V = null

  class SuccessJsonWrapper(result: SuccessResult) {
    def toJson = Json.toJson(result)
  }

  class FailJsonWrapper(result: FailureResult) {
    def toJson = Json.toJson(result)
  }

  implicit def toJson(result: SuccessResult): SuccessJsonWrapper = new SuccessJsonWrapper(result)

  implicit def toJson(result: FailureResult): FailJsonWrapper = new FailJsonWrapper(result)

  def validateThenExecute[T](form: Form[T], success: T => Result)(implicit request: play.api.mvc.Request[_]): Result = {
    form.bindFromRequest.fold(formWithErrors => {
      throw new IllegalArgumentException
    }, success)
  }

  def success() = {
    Ok(new SuccessResult(true, null).toJson)
  }

  def success(message: String) = {
    Ok(new SuccessResult(true, message).toJson)
  }

  def success(view: View) = {
    Ok(new SuccessResult(true, view).toJson)
  }

  def success(views: List[View]) = {
    Ok(new SuccessResult(true, new ListView(false, views)).toJson)
  }

  def success(views: List[View], hasNext: Boolean) = {
    Ok(new SuccessResult(true, new ListView(hasNext, views)).toJson)
  }

  def fail(detail: ErrorDescription) = {
    Ok(new FailureResult(detail).toJson)
  }

  def ensure[T](alias: Any)(any: T): T = {
    require(any != null, s"""'$alias' does not exist.""")
    any
  }
}
