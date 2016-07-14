package controllers

import play.api.mvc.{Action, Controller}

/**
  * Created by Zhenglai on 7/14/16.
  */
class GithubController extends Controller {

  def index = Action {
    Ok(views.html.github())
  }
}
