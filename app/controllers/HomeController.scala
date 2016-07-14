package controllers

import javax.inject._

import play.api.mvc._

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject() extends Controller {

  /**
    * Create an Action to render an HTML page with a welcome message.
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index = Action {
    Ok(views.html.index("Home Scala & Play."))
  }

  // /hello/zhenglai?title=Mr
  def hello(name: String) = Action { request =>
    val title = request.getQueryString("title").map(_+" ").getOrElse("")
    Ok(s"hello $title$name")
  }

  def pictureFor(name: String) = Action {
    Ok(s"Hello $name, this is your picture")
  }

  def hello2(first: String, last: String) = Action {
    Ok(s"Hello $first => $last")
  }

  def json(key: String, value: String) = Action {
    Results.Ok(play.api.libs.json.Json.obj(key -> value));
  }
}
