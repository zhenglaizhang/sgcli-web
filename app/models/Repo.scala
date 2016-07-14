package models

/**
  * Created by Zhenglai on 7/14/16.
  */
case class Repo(
                 val name: String,
                 val language: String,
                 val isFork: Boolean,
                 val size: Long
               )


