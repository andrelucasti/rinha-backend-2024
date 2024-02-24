ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

lazy val root = (project in file("."))
  .settings(
    name := "rinha-backend-2024",
    idePackagePrefix := Some("io.andrelucas")
  )

lazy val zioVersion = "2.1-RC1"

libraryDependencies += "io.javalin" % "javalin" % "6.1.0"
libraryDependencies += "io.javalin" % "javalin-bundle" % "6.1.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.18" % "test"
libraryDependencies += "org.json4s" %% "json4s-native" % "4.0.7"
