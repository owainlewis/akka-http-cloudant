lazy val commonSettings = Seq(
  organization := "io.forward",
  version := "0.1.0",
  scalaVersion := "2.11.8"
)

val akkaVersion = "2.4.7"

val catsVersion = "0.6.0"

lazy val client = (project in file("cloudant-http-client"))
  .settings(commonSettings: _*)
  .settings(
    name := "cloudant-http-client",
    libraryDependencies ++= {
      Seq(
        "com.typesafe.akka" %% "akka-http-core"         % akkaVersion,
        "com.typesafe.akka" %% "akka-http-experimental" % akkaVersion,
        "com.typesafe.akka" %% "akka-stream"            % akkaVersion,
        "org.typelevel"     %% "cats"                   % catsVersion
      )
    }
  )

lazy val cloudantAkkaSprayJson = (project in file("cloudant-akka-spray-json"))
  .dependsOn(client)
  .settings(commonSettings)
  .settings(
    name := "cloudant-akka-spray-json",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion
    )
  )

lazy val root = (project in file("."))
  .aggregate(client)
  .dependsOn(client)
  .settings(commonSettings)
  .settings(
    name := "cloudant-akka-http",
    publish := {}, // skip publishing for this root project.
    publishLocal := {},
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaVersion,
      "io.spray" %%  "spray-json" % "1.3.2"
    )
  )