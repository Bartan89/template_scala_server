package com.example.exploration

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route


object Self_made extends App {

  implicit val system = ActorSystem("HighLevelIntro")



  val simpleRoute : Route =
    path("home") {
      complete(StatusCodes.OK)
    }

  val WithExplicitVerb : Route =
    path("home") {
      post {
        complete(StatusCodes.OK)
      }
    }

  val WithDoubleSlash : Route =
    path("home" / "something") {
      post {
        complete(StatusCodes.OK)
      }
    }

  val WithHtml : Route =
    path("somewhere" / "else") {
      get {
        complete(HttpEntity(
          ContentTypes.`text/html(UTF-8)`,
          """
            |<h1>Hello world</h1>
            |""".stripMargin
        ))
      }
    }

  val SeveralRouter : Route =
    path("somewhere" / "else") {
      get {
        complete(HttpEntity(
          ContentTypes.`text/html(UTF-8)`,
          """
            |<h1>Hello world</h1>
            |""".stripMargin
        ))
      }
    } ~ path("second" / "route") {
      get {
        complete(HttpEntity(
          ContentTypes.`text/html(UTF-8)`,
          """
            |<h2>I am a another path</h2>
            |""".stripMargin
        ))
      }
    }


  Http().newServerAt("localHost", 8080).bindFlow(SeveralRouter)
}

