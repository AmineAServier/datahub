import sbt.Keys._
import sbt._

object Overrides {
  lazy val jackson = "2.12.6"
  private lazy val jacksonCore        = dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-core" % jackson
  private lazy val jacksonDatabind    = dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % jackson
  private lazy val jacksonModuleScala = dependencyOverrides += "com.fasterxml.jackson.module" % "jackson-module-scala_2.12" % jackson

  val jacksonOverrides = Seq(jacksonCore, jacksonDatabind, jacksonModuleScala)
}
