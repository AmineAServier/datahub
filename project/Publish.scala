import sbt.Keys._
import sbt.{addArtifact, _}
import sbtassembly.AssemblyPlugin.autoImport.assembly

import scala.language.postfixOps

object Publish {

  lazy val noop = Seq(
    publish       := Nil,
    publishLocal  := Nil
  )

  lazy val publishAssembly = Seq(artifact in (Compile, assembly) := {
    val art: Artifact = (artifact in (Compile, assembly)).value
    art.withClassifier(classifier = Some("assembly"))},
    addArtifact(artifact in (Compile, assembly), assembly))


  lazy val settings = {
    val nexusRealm = sys.props.get("nexusRealm").getOrElse("nexusRealm is not set")
    val nexusHost = sys.props.get("nexusHost").getOrElse("nexusRealm is not set")
    val nexusUser = sys.props.get("nexusUser").getOrElse("nexusUser is not set")
    val nexusPassword = sys.props.get("nexusPassword").getOrElse("nexusPassword is not set")

    Seq(
      publishMavenStyle := true,
      publishArtifact in (Compile, packageDoc) := false,
      publishArtifact in (Compile, packageSrc) := false,
      publishTo := {
        if (isSnapshot.value)
          Some("snapshots" at "http://" + nexusHost + "/repository/maven-snapshots/")
        else
          Some("releases" at "http://" + nexusHost + "/repository/maven-releases/")
      },
      credentials += Credentials(nexusRealm, nexusHost, nexusUser, nexusPassword)
    )
  }
}