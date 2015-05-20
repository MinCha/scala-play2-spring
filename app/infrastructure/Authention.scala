package infrastructure

import java.util.UUID

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import play.api.mvc.{Request, RequestHeader, Result, WrappedRequest}
import views.shared.View


@JsonIgnoreProperties(ignoreUnknown = true)
case class AuthenticatedUser(id: String)
case class AuthenticatedRequest[A](user: AuthenticatedUser, request: Request[A]) extends WrappedRequest[A](request)


class AuthenticatedView(token: String, userView: View) extends View {
  def getToken = token
  def getUser = userView
}


trait AuthenticatedUserRepository {
  val TokenKey = "X-AUTH-TOKEN"

  def load(request: RequestHeader): Option[AuthenticatedUser]

  def save(token: String, loginUser: AuthenticatedUser, request: RequestHeader, result: Result): Result

  def generateToken: String = {
    UUID.randomUUID.toString
  }
}

class HttpSessionAuthenticatedUserRepository extends AuthenticatedUserRepository {
  def load(request: RequestHeader): Option[AuthenticatedUser] = {
    val token = request.headers.get(TokenKey)

    token match {
      case Some(tokenString) => {
        request.session.get(tokenString).map {
          value =>
            Some(Json.fromJson(value, classOf[AuthenticatedUser]))
        }.getOrElse(None)
      }
      case _ => None
    }
  }

  def save(token: String, loginUser: AuthenticatedUser, request: RequestHeader, result: Result): Result = {
    result.withHeaders(TokenKey -> token).withSession(token -> Json.toJson(loginUser))
  }
}
