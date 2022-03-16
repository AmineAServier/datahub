import sbt.Keys.{version, _}
import sbt._

import scala.collection.immutable.Seq

object ProjectSettings {

  private lazy val general = Seq(
    scalaVersion := "2.12.11",
    organization := "com.github.servier.datahub",
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfuture", "-Xlint"),
    incOptions := incOptions.value,
    doc in Compile := target.value,
    cancelable in Global := true, //allow to use Ctrl + C in sbt prompt
    version := "1.0.0"
  )
  private lazy val shared = general ++ Testing.settings ++ Assembly.assemblySettings ++
    Overrides.jacksonOverrides

  lazy val spark_processing: Seq[Def.SettingsDefinition] = shared
  lazy val sharepoint: Seq[Def.SettingsDefinition] = shared ++ Publish.settings ++ Dependencies.sharepoint
  lazy val sftp: Seq[Def.SettingsDefinition] = shared ++ Publish.settings ++ Dependencies.sftp
  lazy val rest: Seq[Def.SettingsDefinition] = shared ++ Publish.settings ++ Dependencies.union

}
