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

  val complexerRoute : Route =
    path("somewhere" / "else") {
      complete(HttpEntity(
        ContentTypes.`text/html(UTF-8)`,
        """
          |<h1>Hello world</h1>
          |""".stripMargin
      ))
    }


  Http().newServerAt("localHost", 8080).bindFlow(complexerRoute)
}

