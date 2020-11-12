package server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import server.routes_kinda._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.StdIn



object Self_made extends App {

  implicit val system = ActorSystem("HighLevelIntro")



  val bindingFuture = Http().newServerAt("localhost", 8080).bind(doubleRoutes)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when do


}

