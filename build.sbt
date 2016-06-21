name := "akka-http-cloudant"

version := "1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= {
  val akkaVersion = "2.4.7"
  val catsVersion = "0.6.0"
  Seq(
    "com.typesafe.akka" %% "akka-http-core"         % akkaVersion,
    "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
    "com.typesafe.akka" %% "akka-stream"            % akkaVersion,
    "org.typelevel"     %% "cats"                   % catsVersion
  )
}
    