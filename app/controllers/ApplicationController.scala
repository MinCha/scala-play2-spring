package controllers

import play.api.mvc._

object ApplicationController extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }
}