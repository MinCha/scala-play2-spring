package controllers

import infrastructure.{WatchTime, InTransaction}

import scala.collection.JavaConversions._
import com.wordnik.swagger.annotations._
import controllers.shared.BaseController
import domain.{MemoRepository, MemoBookRepository, MemoBookService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc.Action
import views.{MemoDetailView, MemoView}

@Controller
@Api(value = "/memo", description = "memo")
class MemoController(
                      @Autowired memoService: MemoBookService,
                      @Autowired memoBookRepository: MemoBookRepository,
                      @Autowired memoRepository: MemoRepository) extends BaseController {

  @ApiOperation(nickname = "add", value = "add a memo", response = classOf[MemoView], httpMethod = "POST")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "userId", required = true, dataType = "string", paramType = "form"),
    new ApiImplicitParam(name = "memo", required = true, dataType = "string", paramType = "form")))
  def add() = Action(parse.urlFormEncoded) { implicit request =>
    case class Request(userId: String, memo: String)
    val form = Form(mapping(
      "userId" -> nonEmptyText,
      "memo" -> nonEmptyText)(Request.apply)(Request.unapply))
    validateThenExecute[Request](form, r => {
      val result = memoService.add(r.userId, r.memo)
      success(new MemoView(result.lastMemo))
    })
  }

  @ApiOperation(nickname = "get", value = "get all memos", response = classOf[List[MemoView]], httpMethod = "GET")
  def getList(userId: String) = Action { implicit request =>
    val memoBook = ensure(userId) {memoBookRepository.findByUserId(userId)}
    success(memoBook.memos.map(new MemoView(_)).toList)
  }

  @ApiOperation(nickname = "get", value = "get", response = classOf[MemoDetailView], httpMethod = "GET")
  def get(memoId: Long) = Action { implicit request =>
    val memo = ensure(memoId) {memoRepository.findOne(memoId)}

    success(new MemoDetailView(memo))
  }

  @ApiOperation(nickname = "remove", value = "remove a memo", response = classOf[String], httpMethod = "DELETE")
  def remove(memoId: Long) = Action { implicit request =>
    WatchTime("Deleting a memo") {
      memoRepository.delete(memoId)
    }
    success
  }

  def this() = this(null, null, null)
}
