import sbt._

lazy val spark_processing = project.in(file("."))
  .aggregate(sharepoint, sftp)
  .configs(Configs.all: _*)
  .settings(ProjectSettings.spark_processing: _*)
  .dependsOn(sharepoint, sftp)


lazy val sharepoint : Project = project.in(file("sharepoint"))
  .configs(Configs.all: _*)
  .settings(ProjectSettings.sharepoint: _*)

lazy val sftp : Project = project.in(file("sftp"))
  .configs(Configs.all: _*)
  .settings(ProjectSettings.sftp: _*)

lazy val graph : Project = project.in(file("graph"))
  .configs(Configs.all: _*)
  .settings(ProjectSettings.graph: _*)


