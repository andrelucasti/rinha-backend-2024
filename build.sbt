ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.3.1"

lazy val root = (project in file("."))
  .settings(
    name := "rinha-backend-2024",
    idePackagePrefix := Some("io.andrelucas")
  )

lazy val zioVersion = "2.1-RC1"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio-http" % "3.0.0-RC2",
  "dev.zio" %% "zio-http-testkit" % "3.0.0-RC2",
  "dev.zio" %% "zio" % zioVersion,
  "dev.zio" %% "zio-test" % zioVersion,
  "dev.zio" %% "zio-test-sbt" % zioVersion,
  "dev.zio" %% "zio-streams" % zioVersion,
  "dev.zio" %% "zio-test-junit" % zioVersion
)
