package ServerWithDummyData
import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.util.Timeout
import spray.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.io.StdIn


case class Player (nickname : String, characterClass : String, level : Int)

object GameAreaMap {
  case object GetAllPlayers
  case class GetPlayer(nickname: String)
  case class GetPlayersByClass(characterClass : String)
  case class AddPlayer(player : Player)
  case class RemovePlayer(player: Player)
  case object OperationSuccess
}

class GameAreaMap extends Actor with ActorLogging {
  import GameAreaMap._
  var players = Map[String, Player]()

  override def receive: Receive = {
    case GetAllPlayers =>
      log.info("Getting all players")
      sender() ! players.values.toList
    case GetPlayer(nickname) =>
      log.info(s"getting player with nickname $nickname")
      sender() ! players.get(nickname)
    case GetPlayersByClass(characterClass) =>
      log.info(s"Getting all players with the character class: $characterClass")
      sender() ! players.values.toList.filter(_.characterClass == characterClass)
    case AddPlayer(player) =>
      log.info(s"trying to add player $player")
      players = players + (player.nickname -> player)
      sender() ! OperationSuccess
    case RemovePlayer(player) =>
      log.info(s"trying to remove $player")
      players = players - player.nickname
      sender() ! OperationSuccess
    }
}

trait PlayerJsonProtocol extends DefaultJsonProtocol {
  implicit val playerFormat = jsonFormat3(Player)
}

object OneFile extends App with PlayerJsonProtocol with SprayJsonSupport {


  implicit val system = ActorSystem("ServerWithDummyData")
  import GameAreaMap._

  val rtjvmGameMap = system.actorOf(Props[GameAreaMap], "rockTheJVMAreaMap")
  val playerList = List(
    Player("JumboJetter", "warrior", 80),
    Player("DarbDom", "magician", 60),
    Player("DunkinDanny", "warrior", 80)
  )

  playerList.foreach { player =>
    rtjvmGameMap ! AddPlayer(player)
  }

  val simpleRoute: Route =
    path("home") {
      complete("hello world")
    }


  implicit val timeout = Timeout(2 seconds)
  val routesForGame =
    pathPrefix("api" / "player") {
      get {
        path("class" / Segment) { characterClass =>
          val playersByClassFuture = (rtjvmGameMap ? GetPlayersByClass(characterClass)).mapTo[List[Player]]
          complete(playersByClassFuture)
        }
      }
    }


  val bindingFuture = Http().newServerAt("localhost", 8080).bind(routesForGame)




  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when do

}
