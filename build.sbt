lazy val akkaHttpVersion = "10.2.1"
lazy val akkaVersion    = "2.6.10"

lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
      organization    := "com.example",
      scalaVersion    := "2.13.3"
    )),
    name := "tuetryserver",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"                % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json"     % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-actor-typed"         % akkaVersion,
      "com.typesafe.akka" %% "akka-stream"              % akkaVersion,
      "ch.qos.logback"    % "logback-classic"           % "1.2.3",
      "com.typesafe.akka" %% "akka-http-spray-json"     % "10.1.7",


      "com.typesafe.akka" %% "akka-stream-testkit" % "2.6.8",
      "com.typesafe.akka" %% "akka-http-testkit" % "10.2.1",
    )
  )

