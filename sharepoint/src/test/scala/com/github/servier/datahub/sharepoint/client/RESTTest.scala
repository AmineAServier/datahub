package com.github.servier.datahub.sharepoint.client

import com.github.servier.datahub.sharepoint.util.{DfsUtils, FileTransferOptions}
import com.holdenkarau.spark.testing.DataFrameSuiteBase
import org.apache.hadoop.fs.Path
import org.apache.spark.sql.{DataFrame, DataFrameReader}
import org.scalatest.FunSuite

import java.util.UUID

/**
  * The `REST` class provides `upload` and `download` methods
  * to transfer files to/from remote host via '''REST'''.
  *
  * @constructor Create a new `REST` by specifying their `options`.
  * @param options Options passed by the user on the
  *                '''DataFrameReader''' or '''DataFrameWriter''' API.
  * @since 0.1.0
  */
class RESTTest extends FunSuite with DataFrameSuiteBase {
  ignore("niouk3") {
    val params: Map[String, String] =
      Map(
        "grant_type" -> "client_credentials",
        "resource" -> "00000003-0000-0ff1-ce00-000000000000/infinity2568.sharepoint.com@eed4540e-335e-4540-954e-6c039c21893b",
        "client_id" -> "52d25999-b6b2-4a19-b544-d70f77dc4225@eed4540e-335e-4540-954e-6c039c21893b",
        "client_secret" -> "Wev7Q~X_JCnePKFebZImbE3qzNwt3VM6ggENT",
        "fileFormat" -> "csv",
        "inferSchema" -> "false",
        "url" -> "https://infinity2568.sharepoint.com/sites/test/",
        "source_path" -> "/sites/test/Documents%20partages/sodainput.csv",
        "file_name" -> "sodainput.csv",
        "tenant_id" -> "eed4540e-335e-4540-954e-6c039c21893b"
      )
    val options: FileTransferOptions = new FileTransferOptions(params)
    val dfsTempDir: Path = new Path(
      options.dfsTempPath,
      UUID.randomUUID().toString
    )
    val toto = new Sharepoint(options)
    toto.uploadFile
    val df: DataFrame = {
      var dfr: DataFrameReader = spark.read
      val dfs: DfsUtils = new DfsUtils(sqlContext)
      dfs.copyFromLocal(
        s"${options.localTempPath}${options.file_name.get}",
        dfsTempDir.toString
      )
      dfr
        .options(options.dfOptions)
        .format("csv")
        .load(dfsTempDir.toString)
    }
    df.show()
  }

}
