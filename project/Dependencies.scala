import sbt.*

object Dependencies {
  val zioVersion = "2.0.10"
  val circeVersion = "0.14.6"
  val http4sVersion = "0.23.14"

  def zioModule(module: String): ModuleID = "dev.zio" %% s"zio$module" % zioVersion

  def circeModule(module: String): ModuleID = "io.circe" %% s"circe-$module" % circeVersion

  def http4sModule(module: String): ModuleID = "org.http4s" %% s"http4s-$module" % http4sVersion

  lazy val zio: Seq[ModuleID] = Seq(
    zioModule(""),
    zioModule("-macros")
  )

  lazy val circe: Seq[ModuleID] = Seq(
    circeModule("core"),
    circeModule("generic")
  )

  lazy val http4s: Seq[ModuleID] = Seq(
    http4sModule("dsl"),
    http4sModule("circe"),
    http4sModule("ember-server")
  )
}
