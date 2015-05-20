package controllers.shared

import domain.exception.{InvaildParameterException, UnauthorizedException}
import infrastructure.{AuthenticatedRequest, AuthenticatedUser, HttpSessionAuthenticatedUserRepository}
import play.api.Logger
import play.api.data.Form
import play.api.mvc._

import scala.concurrent.Future

trait BaseAction {
  private def bindFromRequest[T](implicit request: play.api.mvc.Request[_], formMapping: play.api.data.Mapping[T]): T = {
    val form = Form(formMapping)

    form.bindFromRequest.fold(
      formWithErrors => {
        throw new InvaildParameterException
      },
      formData => formData)
  }

  def withUser[A](bodyParser: play.api.mvc.BodyParser[A])(f: AuthenticatedUser => Request[A] => Result): EssentialAction = {
    (LoggingAction andThen LoginAction)(bodyParser) {
      request => f(request.user)(request)
    }
  }

  def withUser(f: AuthenticatedUser => Request[AnyContent] => Result): EssentialAction = {
    withUser(play.api.mvc.BodyParsers.parse.anyContent)(f)
  }

  def withUserAndForm[T, A](bodyParser: play.api.mvc.BodyParser[A], formMapping: play.api.data.Mapping[T])(f: AuthenticatedUser => T => Request[A] => Result): EssentialAction = {
    withUser(bodyParser) {
      user => request => {
        val formData: T = bindFromRequest(request, formMapping)
        f(user)(formData)(request)
      }
    }
  }

  def withUserAndForm[T](formMapping: play.api.data.Mapping[T])(f: AuthenticatedUser => T => Request[AnyContent] => Result): EssentialAction = {
    withUserAndForm(play.api.mvc.BodyParsers.parse.anyContent, formMapping)(f)
  }

  def withForm[T, A](bodyParser: play.api.mvc.BodyParser[A], formMapping: play.api.data.Mapping[T])(f: T => Request[A] => Result): EssentialAction = {
    Action(bodyParser) {
      request => {
        val formData: T = bindFromRequest(request, formMapping)
        f(formData)(request)
      }
    }
  }

  def withForm[T](formMapping: play.api.data.Mapping[T])(f: T => Request[AnyContent] => Result): EssentialAction = {
    withForm(play.api.mvc.BodyParsers.parse.anyContent, formMapping)(f)
  }
}

object LoggingAction extends ActionBuilder[Request] with ActionTransformer[Request, Request] {
  override protected def transform[A](request: Request[A]): Future[Request[A]] = {
    Logger.info(request.uri)

    Future.successful(request)
  }
}

object LoginAction extends ActionBuilder[AuthenticatedRequest] with ActionTransformer[Request, AuthenticatedRequest] {
  val authenticatedUserRepository = new HttpSessionAuthenticatedUserRepository()

  override protected def transform[A](request: Request[A]): Future[AuthenticatedRequest[A]] = {
    val user = authenticatedUserRepository.load(request)

    if (user.isDefined == false) {
      throw new UnauthorizedException()
    }

    Future.successful {
      AuthenticatedRequest[A](user.get, request)
    }
  }
}