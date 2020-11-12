package com.example.exploration

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route

import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.StdIn



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

  val doubleRoutes : Route =
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

  val extractInt : Route =
    path("yourage" / IntNumber) {(age : Int) =>
      get {
        println("can I print here? yes")
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`,
          s"""
            | <h2>you are $age years old</h2>
            |""".stripMargin))
      }
    }

  val extractMoreInts : Route =
    path("yourage" / IntNumber / IntNumber) {(age : Int, favNumber : Int) =>
      get {
        println("can I print here? yes")
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`,
          s"""
             | <h2>you are $age years old</h2>
             | <h4>your fav number is $favNumber</h4>
             |""".stripMargin))
      }
    }



  val readQueryParam =
    path("your") {
      get {
        parameter("name".as[String]) { (name : String) =>
          complete(HttpEntity(ContentTypes.`text/html(UTF-8)`,
            s"""
               | <h2>your name is: $name</h2>
               |""".stripMargin))
        }
       }
      }


  val compactNotation = (path("compact") & get) {
    complete("hello world")
  }


  val returnText : String = "hello world"

  val dryNotationPaths = (path("bart") | path("kuijper")) { complete(returnText) }



  val bindingFuture = Http().newServerAt("localhost", 8080).bind(dryNotationPaths)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when do


}

