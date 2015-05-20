package controllers

import com.wordnik.swagger.annotations._
import controllers.shared.BaseController
import domain.{MemoBookRepository, MemoBookService, MemoRepository}
import infrastructure.WatchTime
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import play.api.data.Forms._
import views.{MemoDetailView, MemoView}

import scala.collection.JavaConversions._

@Controller
@Api(value = "/memo", description = "memo")
class MemoController(
                      @Autowired memoBookService: MemoBookService,
                      @Autowired memoBookRepository: MemoBookRepository,
                      @Autowired memoRepository: MemoRepository) extends BaseController {
  case class MemoAddRequest(memo: String)
  val MemoAddRequestMapping = mapping(
    "memo" -> nonEmptyText)(MemoAddRequest.apply)(MemoAddRequest.unapply)

  @ApiOperation(nickname = "add", value = "add a memo", response = classOf[MemoView], httpMethod = "POST")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "X-AUTH-TOKEN", required = true, dataType = "string", paramType = "header"),
    new ApiImplicitParam(name = "memo", required = true, dataType = "string", paramType = "form")))
  def add = withUserAndForm(parse.urlFormEncoded, MemoAddRequestMapping) { user => form => implicit request =>
    val result = memoBookService.add(user.id, form.memo)
    success(new MemoView(result.lastMemo))
  }

  @ApiOperation(nickname = "get", value = "get all memos", response = classOf[List[MemoView]], httpMethod = "GET")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "X-AUTH-TOKEN", required = true, dataType = "string", paramType = "header")))
  def getList = withUser { user => implicit request =>
    val memoBook = memoBookRepository.findByUserId(user.id)
    success(memoBook.memos.map(new MemoView(_)).toList)
  }

  @ApiOperation(nickname = "get", value = "get", response = classOf[MemoDetailView], httpMethod = "GET")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "X-AUTH-TOKEN", required = true, dataType = "string", paramType = "header")))
  def get(memoId: Long) = withUser { user => implicit request =>
    val memo = ensure(memoId) {memoRepository.findOne(memoId)}
    success(new MemoDetailView(memo))
  }

  @ApiOperation(nickname = "remove", value = "remove a memo", response = classOf[String], httpMethod = "DELETE")
  @ApiImplicitParams(Array(
    new ApiImplicitParam(name = "X-AUTH-TOKEN", required = true, dataType = "string", paramType = "header")))
  def remove(memoId: Long) = withUser { user => implicit request =>
    WatchTime("Deleting a memo") {
      memoRepository.delete(memoId)
    }
    success
  }

  def this() = this(null, null, null)
}
