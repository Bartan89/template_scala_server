package ServerWithDummyData
import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout
import spray.json._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration.DurationInt
import scala.io.StdIn


case class Patient (id: Int, patient : String, age : Int, disease : String)

object HospitalImaginary {
  case object GetAllPatients
  case class GetPatient(id: Int)
  case class GetPatientsByCovid(disease : String)
  case class AddPatient(patient : Patient)
  case class RemovePlayer(patient: Patient)
  case object OperationSuccess
}


class HospitalImaginary extends Actor with ActorLogging {
  import HospitalImaginary._
  var patients = Map[Int, Patient]()

  override def receive: Receive = {
    case GetAllPatients =>
      log.info("Getting all patients")
      sender() ! patients.values.toList
    case GetPatient(id) =>
      log.info(s"getting patient with id $id")
      sender() ! patients.get(id)
    case GetPatientsByCovid(disease) =>
      log.info(s"Getting patient with $disease")
      sender() ! patients.values.toList.filter(_.disease == disease)
    case AddPatient(patient) =>
      log.info(s"trying to add player $patient")
      patients = patients + (patient.id -> patient)
      sender() ! OperationSuccess
    case RemovePlayer(patient) =>
      log.info(s"trying to remove $patient")
      patients = patients - patient.id
      sender() ! OperationSuccess
    }
}

trait PatientJsonProtocol extends DefaultJsonProtocol {
  implicit val patientFormat = jsonFormat4(Patient)
}

object OneFile extends App with PatientJsonProtocol with SprayJsonSupport {


  implicit val system = ActorSystem("ServerWithDummyData")
  import HospitalImaginary._

  val hospital = system.actorOf(Props[HospitalImaginary], "hospital")
  val playerList = List(
    Patient(1, "Marie",  31, "diabetes" ),
    Patient(2, "Frits",  73, "cancer" ),
    Patient(3, "Erik",  65, "diabetes" )
  )

  playerList.foreach { patient =>
    hospital ! AddPatient(patient)
  }


  //http localhost:8080/api/patients/disease/diabetes

  implicit val timeout = Timeout(1 seconds)

  val routesForGame =
    pathPrefix("api"./("patients")) {
      get {
        path("disease" / Segment) { disease =>
          val patientByCovid = (hospital ? GetPatientsByCovid(disease)).mapTo[List[Patient]]
          val oldPatients = patientByCovid.map(_.map(_.copy(age = 100)))
          //timeout 2 second
          println("iets" + patientByCovid)
          val firstPatient : Future[Patient] = patientByCovid.map( x => x.head)
          val olderFirstPatient = firstPatient.map(_.copy(age = 100))
          println(firstPatient)
          println(olderFirstPatient)
          complete(patientByCovid)
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
