package controllers

import com.wordnik.swagger.annotations.{Api, ApiImplicitParam, ApiImplicitParams, ApiOperation}
import controllers.shared._
import domain.exception.ExceptionType
import domain.{User, UserRepository}
import infrastructure._
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import play.api.data.Forms._
import play.api.mvc.RequestHeader
import views.UserView


@Controller
@Api(value = "/auth", description = "auth")
class AuthenticationController(
                      @Autowired userRepository: UserRepository,
                      authenticatedUserRepository: AuthenticatedUserRepository = new HttpSessionAuthenticatedUserRepository()) extends BaseController {
  case class SignupRequest(id: String, password: String, name: String)
  case class SigninRequest(id: String, password: String)

  val SignupRequestMapping = mapping(
    "id" -> nonEmptyText,
    "password" -> nonEmptyText,
    "name" -> nonEmptyText)(SignupRequest.apply)(SignupRequest.unapply)

  val SigninRequestMapping = mapping(
    "id" -> nonEmptyText,
    "password" -> nonEmptyText)(SigninRequest.apply)(SigninRequest.unapply)

  @ApiOperation(nickname = "sign up", value = "sign up as a user", response = classOf[String], httpMethod = "POST")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "id", required = true, dataType = "string", paramType = "form"),
    new ApiImplicitParam(name = "password", required = true, dataType = "string", paramType = "form"),
    new ApiImplicitParam(name = "name", required = true, dataType = "string", paramType = "form")))
  def signup() = withForm(SignupRequestMapping) { form => implicit request =>
    val dbUser = userRepository.findOne(form.id)

    if (dbUser != null) {
      fail(ErrorDescription(ExceptionType.AlreadyRegistered, "Already registered", ""))
    } else {
      val newUser = userRepository.save(new User(form.id, form.password, form.name))
      successLogin(newUser)
    }
  }

  @ApiOperation(nickname = "sign in", value = "sign in", response = classOf[String], httpMethod = "POST")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "id", required = true, dataType = "string", paramType = "form"),
    new ApiImplicitParam(name = "password", required = true, dataType = "string", paramType = "form")))
  def signin() = withForm(SigninRequestMapping) { form => implicit request =>
    val user = userRepository.findOne(form.id)

    if (user == null) {
      fail(ErrorDescription(ExceptionType.Unregistered, "User not found", ""))
    } else {
      if (user.password == form.password) {
        successLogin(user)
      } else {
        fail(ErrorDescription(ExceptionType.Unauthorized, "Invalid password", ""))
      }
    }
  }

  private def successLogin(user: User)(implicit request: RequestHeader) = {
    val token = authenticatedUserRepository.generateToken
    val result = success(new AuthenticatedView(token, new UserView(user)))

    authenticatedUserRepository.save(token, AuthenticatedUser(user.id), request, result)
  }

  def this() = this(null)
}
