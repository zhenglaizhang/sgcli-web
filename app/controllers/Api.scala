package controllers

import play.api.libs.json.Writes
import play.api.mvc.Controller

// query external apis

/**
  * Created by Zhenglai on 7/14/16.
  */
class Api extends Controller {

  import models.Repo
  import play.api.mvc.Action

  // dummy data
  val data = List[Repo](
    Repo("dotty", "scala", true, 14315),
    Repo("frontend", "JavaScript", false, 392)
  )

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

  // controller
  def repos(userName: String) = Action {
    import play.api.libs.json.Json
    val repoArray = Json.toJson(data)
    // toJson(data) relies on the existence of `Writes[List[Repo]]` type class in scope
    Ok(repoArray)
  }

}
