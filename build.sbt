scalaVersion := "2.13.8"

name := "zio-http4s"
organization := "ru.arlen"
version := "1"

libraryDependencies ++= Dependencies.zio
libraryDependencies ++= Dependencies.circe
libraryDependencies ++= Dependencies.http4s
libraryDependencies += "dev.zio" %% "zio-interop-cats" % "23.0.03"

scalacOptions += "-Ymacro-annotations"

enablePlugins(JavaAppPackaging)
enablePlugins(DockerPlugin)
enablePlugins(AshScriptPlugin)

dockerBaseImage       := "openjdk:jre-alpine"

mainClass := Some("ru.arlen.phonebook.Main")