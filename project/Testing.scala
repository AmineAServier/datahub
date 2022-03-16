import sbt.Keys._
import sbt._

object Testing {

  import BuildKeys._
  import Configs._

  private lazy val testSettings = Seq(
    fork in Test := true,
    parallelExecution in Test := false
  )

  private lazy val itSettings = inConfig(IntegrationTest)(Defaults.testSettings) ++ Seq(
    fork in IntegrationTest := true,
    parallelExecution in IntegrationTest := false,
    scalaSource in IntegrationTest := baseDirectory.value / "src/it/scala"
  )

  lazy val settings = testSettings ++ itSettings ++ Seq(
    testAll := ((): Unit),
    testAll := testAll.dependsOn(test in IntegrationTest).value,
    testAll := testAll.dependsOn(test in Test).value
  )
}
