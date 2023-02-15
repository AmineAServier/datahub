import sbt.Keys._
import sbt.{ModuleID, _}

object Dependencies {

  private def depends(modules: ModuleID*): Seq[Setting[_]] = Seq(libraryDependencies ++= modules)
  
  val typesafeConfig = "com.typesafe" % "config" % "1.3.1"
  val playJson = "com.typesafe.play" %% "play-json" % "2.9.2"
  val lazyLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"
  val mockito = "org.mockito" % "mockito-core" % "2.8.9" % Test
  val scalaTest = "org.scalatest" %% "scalatest" % "3.0.5" % Test
  val awsCore = "com.amazonaws" % "aws-java-sdk-core" % "1.11.95"
  val awsS3 = "com.amazonaws" % "aws-java-sdk-s3" % "1.11.95"
  val awsSSM = "com.amazonaws" % "aws-java-sdk-ssm" % "1.11.764"
  val msSQLServer = "com.microsoft.azure" % "spark-mssql-connector" % "1.0.0"
  val scalajHTTP = "org.scalaj" % "scalaj-http_2.12" % "2.4.2"
  val jsch = "com.jcraft" % "jsch" % "0.1.55"


  val sftp: Seq[sbt.Setting[_]] = depends(typesafeConfig, lazyLogging, mockito, scalaTest, Spark.sparkHive, Spark.sparkCore, Spark.sparkSql, Spark.sparkTesting, Jackson.jacksonCore, Jackson.jacksonModuleScala, Jackson.jacksonDatabind,jsch)
  val sharepoint: Seq[sbt.Setting[_]] = depends(typesafeConfig, lazyLogging, mockito, scalaTest, Spark.sparkHive, Spark.sparkCore, Spark.sparkSql, Spark.sparkTesting, Jackson.jacksonCore, Jackson.jacksonModuleScala, Jackson.jacksonDatabind, scalajHTTP, playJson)
  val graph: Seq[sbt.Setting[_]] = depends(typesafeConfig, lazyLogging, mockito, scalaTest, Spark.sparkHive, Spark.sparkCore, Spark.sparkSql, Spark.sparkTesting, Jackson.jacksonCore, Jackson.jacksonModuleScala, Jackson.jacksonDatabind, scalajHTTP, playJson)

  val latest_snapshot: Seq[sbt.Setting[_]] = depends(typesafeConfig, lazyLogging, mockito, scalaTest, Spark.sparkHive, Spark.sparkCore, Spark.sparkSql, Spark.sparkTesting, Spark.sparkAvro, Spark.sparkDelta, Jackson.jacksonCore, Jackson.jacksonModuleScala, Jackson.jacksonDatabind)
  val lookup_table: Seq[sbt.Setting[_]] = depends(typesafeConfig, lazyLogging, mockito, scalaTest, Spark.sparkHive, Spark.sparkCore, Spark.sparkSql, Spark.sparkTesting, Jackson.jacksonCore, Jackson.jacksonModuleScala, Jackson.jacksonDatabind)
  val delta: Seq[sbt.Setting[_]] = depends(typesafeConfig, lazyLogging, mockito, scalaTest, Spark.sparkHive, Spark.sparkCore, Spark.sparkSql, Spark.sparkTesting, Jackson.jacksonCore, Jackson.jacksonModuleScala, Jackson.jacksonDatabind, awsCore, awsS3)
  val filter: Seq[sbt.Setting[_]] = depends(typesafeConfig, lazyLogging, mockito, scalaTest, Spark.sparkHive, Spark.sparkCore, Spark.sparkSql, Spark.sparkTesting, Jackson.jacksonCore, Jackson.jacksonModuleScala, Jackson.jacksonDatabind, awsCore, awsS3)
  val publish_kafka: Seq[sbt.Setting[_]] = depends(typesafeConfig, lazyLogging, mockito, scalaTest, Spark.sparkHive, Spark.sparkCore, Spark.sparkSql, Spark.sparkTesting, Spark.sparkSQLKafka, Jackson.jacksonCore, Jackson.jacksonModuleScala, Jackson.jacksonDatabind, awsCore, awsS3)
  val union: Seq[sbt.Setting[_]] = depends(typesafeConfig, lazyLogging, mockito, scalaTest, Spark.sparkHive, Spark.sparkCore, Spark.sparkSql, Spark.sparkTesting, Spark.sparkSQLKafka, Jackson.jacksonCore, Jackson.jacksonModuleScala, Jackson.jacksonDatabind,  awsCore, awsS3, awsSSM, msSQLServer)

  object Spark {
    val version = "3.2.0"
    val sparkCore = "org.apache.spark" %% "spark-core" % version % Provided
    val sparkSql = "org.apache.spark" %% "spark-sql" % version % Provided
    val sparkStreaming = "org.apache.spark" %% "spark-streaming" % version % Provided
    val sparkStreamingKafka = "org.apache.spark" %% "spark-streaming-kafka-0-10" % version
    val sparkSQLKafka = "org.apache.spark" %% "spark-sql-kafka-0-10" % version
    val sparkAvro = "org.apache.spark" %% "spark-avro" % version 
    val sparkTesting = "com.holdenkarau" %% "spark-testing-base" % "3.2.0_1.1.1" % Test
    val sparkHive = "org.apache.spark" %% "spark-hive" % version % Test
    val sparkDelta = "io.delta" %% "delta-core" % "0.6.0" % Test
  }

  object Jackson {
    val version = "2.12.6"
    val jacksonCore = "com.fasterxml.jackson.core" % "jackson-core" % version
    val jacksonModuleScala = "com.fasterxml.jackson.module" %% "jackson-module-scala" % version
    val jacksonDatabind = "com.fasterxml.jackson.core" % "jackson-databind" % version
  }

}
