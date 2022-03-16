import sbt.Keys.{name, version}
import sbt.Setting
import sbtassembly.AssemblyPlugin.autoImport.{assembly, assemblyJarName, assemblyMergeStrategy, assemblyShadeRules}
import sbtassembly.{MergeStrategy, PathList, ShadeRule}

object Assembly {
  private lazy val jarName = assemblyJarName in assembly := { s"${name.value}-assembly-${version.value}.jar" }

  private lazy val mergeStrategySetting: Setting[String => MergeStrategy] = assemblyMergeStrategy in assembly := {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case PathList("org","aopalliance", xs @ _*)                  => MergeStrategy.last
    case PathList("javax", "inject", xs @ _*)                    => MergeStrategy.last
    case PathList("org", "apache", xs @ _*)                      => MergeStrategy.last
    case PathList("ch","qos","logback", xs @ _*)                 => MergeStrategy.first
    case PathList("org", "slf4j", "slf4j-log4j12", xs @ _*)      => MergeStrategy.last
    case PathList("com", "codhale", "metrics", xs @ _*)          => MergeStrategy.discard
    case PathList("io", "dropwizard", "metrics", xs @ _*)        => MergeStrategy.discard
    case PathList("org", "apache", "calcite", xs @ _*)           => MergeStrategy.discard
    case PathList("org", "glassfish", "jersey", "core", xs @ _*) => MergeStrategy.discard
    case PathList("com", "sun", "jersey", xs @ _*)               => MergeStrategy.discard
    case PathList("javax", "ws", "rs", xs @ _*)                  => MergeStrategy.discard
    case PathList("stax", xs @ _*)                               => MergeStrategy.discard
    case PathList("xml-apis", xs @ _*)                           => MergeStrategy.discard
    case PathList("javax", "xml", "stream", xs @ _*)             => MergeStrategy.discard
    case PathList("org", "ow2", "asm", xs @ _*)                  => MergeStrategy.discard
    case PathList("org", "objenesis", xs @ _*)                   => MergeStrategy.discard
    //case other                                                   => (assemblyMergeStrategy in assembly).value.apply(other)
    case _ => MergeStrategy.first
  }

  private lazy val shadeRule = assemblyShadeRules in assembly := Seq(
    ShadeRule.rename("shapeless.**" -> "shadeshapless.@1").inAll
  )

  lazy val assemblySettings = Seq(mergeStrategySetting, jarName, shadeRule)
}

