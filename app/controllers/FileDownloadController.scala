package controllers

import java.io.File

import com.wordnik.swagger.annotations._
import controllers.shared.BaseController
import org.springframework.stereotype.Controller
import play.api.data.Forms._

@Controller
@Api(value = "/memo", description = "memo")
class FileDownloadController extends BaseController {

  case class FileDownLoadRequest(filename: String)

  val FileDownload = mapping(
    "filename" -> nonEmptyText)(FileDownLoadRequest.apply)(FileDownLoadRequest.unapply)

  def download = withForm(FileDownload) { form => implicit request =>
    Ok.sendFile(content = new File(form.filename))
  }
}
