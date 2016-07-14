package controllers

import models.Repo
import play.api.Play.current
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.libs.ws.WS
import play.api.mvc.{Action, Controller}

/**
  * Created by Zhenglai on 7/14/16.
  */
class ApiV2 extends Controller {

  // type class for converting Repo -> JSON
  // or we could move this to model's companion objects
  implicit val writesRepos = new Writes[Repo] {

    import play.api.libs.json.Json

    override def writes(repo: Repo) = Json.obj(
      "name" → repo.name,
      "language" → repo.language,
      "is_fork" → repo.isFork,
      "size" → repo.size
    )
  }

  // demo JSResult parsing
  private def demo1() = {
    val s = """{"name": "zhenglai", "age": 27}""";
    val js = Json.parse(s);
    js \ "name" // JsDefined or JsUndefined => JsLokupResult
    (js \ "name").validate[String] // JsSuccess or JsError => JsResult[T]
    val name = (js \ "name").validate[String] match {
        case JsSuccess(value, _) ⇒ value
        case JsError(e) ⇒ throw new IllegalStateException(s"Error extracting naem: $e")
      }

  }

  // type class for github JSON -> Repo conversion
  implicit val readsRepoFromGithub: Reads[Repo] = (
    (JsPath \ "name").read[String] and
      (JsPath \ "lanauage").read[String] and
      (JsPath \ "fork").read[Boolean] and
      (JsPath \ "size").read[Long]
    ) (Repo.apply _)

  // controller
  def reposAsync(userName: String) = Action.async { request ⇒
    val url = s"https://api.github.com/users/$userName/repos"
    var ws = WS.url(url)
    val token = request.getQueryString("token").foreach { token ⇒
      ws = ws.withHeaders("Authorization" → s"token $token")
    }
    ws = ws.withHeaders("Content-Type" → "application/json")
    val response = {
      ws.get()
    }

    // response is a future
    response map { r ⇒
      if (r.status == 200) {
        val reposOpt = Json.parse(r.body).validate[List[Repo]] // Reads[Repo] type class is implicitly avaliable in teh current namespace
        reposOpt match {
          case JsSuccess(repos, _) ⇒ Ok(Json.toJson(repos))
          case _ ⇒ InternalServerError
        }
      } else {
        // github returned something other than 200
        NotFound
      }
    }
  }

}
