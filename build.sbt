ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

lazy val root = (project in file("."))
  .settings(
    name := "rinha-backend-2024",
    idePackagePrefix := Some("io.andrelucas")
  )

libraryDependencies += "io.javalin" % "javalin" % "6.1.0"
libraryDependencies += "io.javalin" % "javalin-bundle" % "6.1.0"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.18" % "test"
libraryDependencies += "org.json4s" %% "json4s-native" % "4.0.7"
libraryDependencies += "com.typesafe.slick" %% "slick" % "3.5.0-RC1"
libraryDependencies += "org.postgresql" % "postgresql" % "42.7.1"