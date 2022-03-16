package com.github.servier.datahub.sharepoint.util

import org.apache.spark.internal.Logging
import org.apache.spark.sql.catalyst.util.CaseInsensitiveMap

/**
  * @constructor Create a new `FileTransferOptions` by specifying their `parameters`.
  *
  * @param parameters Options specified on the '''DataFrame API''' Reader/Writer.
  *
  * @since 0.1.0
  */
class FileTransferOptions(
    @transient private val parameters: CaseInsensitiveMap[String]
) extends Serializable
    with Logging {

  import FileTransferOptions._

  /**
    * @constructor Create a new `FileTransferOptions` by specifying their `parameters`.
    *
    * @param parameters Options specified on the '''DataFrame API''' Reader/Writer.
    *
    * @since 0.1.0
    */
  def this(parameters: Map[String, String]) =
    this(CaseInsensitiveMap(parameters))

  /**
    * Value set with the option key __protocol__.
    * Default value set to ''sharepoint''.
    *
    * @since 0.1.0
    */
  val protocol: Protocol.Value = Protocol.withName(
    parameters.getOrElse(PROTOCOL, Protocol.sharepoint.toString).toLowerCase
  )
  val fileFormat: FileFormat.Value = FileFormat.withName(
    parameters
      .getOrElse(
        FILE_FORMAT,
        sys.error(
          s"Missing '$FILE_FORMAT' option with one of ${FileFormat.values.toList} value!!"
        )
      )
      .toLowerCase
  )

  /**
    * Value set with the option key __localTempPath__.
    * Default value set to java system property of `java.io.tmpdir`
    * with a fallback value of `/tmp`.
    *
    * @since 0.1.0
    */
  val localTempPath: String = parameters.getOrElse(
    LOCAL_TEMP_PATH,
    System.getProperty("java.io.tmpdir", "/tmp")
  )

  /**
    * Value set with the option key __dfsTempPath__.
    * Default value set to option value from `localTempPath`.
    *
    * @note If '''dfsTempPath''' is not provided then the downloaded
    *       file from remote host would be on the local path which
    *       is not splittable and therefore, parallelism might not
    *       be achieved during the file scan to load data.
    *
    * @since 0.1.0
    */
  val dfsTempPath: String = parameters.getOrElse(DFS_TEMP_PATH, localTempPath)

  /**
    * Value set with the option key __uploadFilePrefix__.
    * Default value set to `part`.
    *
    * @since 0.1.0
    */
  val uploadFilePrefix: String = parameters.getOrElse(
    UPLOAD_FILE_PREFIX,
    "part"
  )

  /**
    * All other options except the extracted options from the above
    * to be passed along to the '''DataFrame API''' for read/write.
    * For example, csv options like ''delimiter'' for the `csv` file format etc.
    *
    * @since 0.1.0
    */
  val dfOptions: Map[String, String] = parameters -- excludeOptionsToDataFrame

  /**
    * Value set with the option key client_id.
    * Default value set to `part`.
    *
    * @since 0.1.0
    */
  val client_id: Option[String] = parameters.get(
    CLIENT_ID
  )

  /**
    * Value set with the option key client_secret.
    * Default value set to `part`.
    *
    * @since 0.1.0
    */
  val client_secret: Option[String] = parameters.get(CLIENT_SECRET)

  /**
    * Value set with the option key grant_type.
    * Default value set to `part`.
    *
    * @since 0.1.0
    */
  val grant_type: Option[String] = parameters.get(GRANT_TYPE)

  /**
    * Value set with the option key resource.
    * Default value set to `part`.
    *
    * @since 0.1.0
    */
  val resource: Option[String] = parameters.get(RESOURCE)

  /**
    * Value set with the option key uri.
    * Default value set to `part`.
    *
    * @since 0.1.0
    */
  val uri: Option[String] = parameters.get(URI)

  /**
    * Value set with the option key method.
    * Default value set to `part`.
    *
    * @since 0.1.0
    */
  val method: Option[String] = parameters.get(METHOD)

  /**
    * Value set with the option key resource.
    * Default value set to `data`.
    *
    * @since 0.1.0
    */
  val data: Option[String] = parameters.get(DATA)

  /**
    * Value set with the option key resource.
    * Default value set to `output_path`.
    *
    * @since 0.1.0
    */
  val output_path: Option[String] = parameters.get(OUTPUT_PATH)

  /**
    * Value set with the option key resource.
    * Default value set to `output_path`.
    *
    * @since 0.1.0
    */
  val tenant_id: Option[String] = parameters.get(TENANT_ID)

  /**
    * Value set with the option key uri.
    * Default value set to `part`.
    *
    * @since 0.1.0
    */
  val url: Option[String] = parameters.get(URL)

  /**
    * Value set with the option key uri.
    * Default value set to `part`.
    *
    * @since 0.1.0
    */
  val source_path: Option[String] = parameters.get(SOURCE_PATH)

  /**
    * Value set with the option key uri.
    * Default value set to `part`.
    *
    * @since 0.1.0
    */
  val file_name: Option[String] = parameters.get(FILE_NAME)

}

/**
  * Constants for the spark file transfer option keys
  * passed with the '''DataFrame API'''.
  *
  * @since 0.1.0
  */
object FileTransferOptions {

  /**
    * Constant for the option key __protocol__.
    *
    * @since 0.1.0
    */
  val PROTOCOL: String = "protocol"

  /**
    * Constant for the option key __fileFormat__.
    *
    * @since 0.1.0
    */
  val FILE_FORMAT: String = "fileFormat"

  /**
    * Constant for the option key __path__.
    *
    * @since 0.1.0
    */
  val PATH: String = "path"

  /**
    * Constant for the option key __localTempPath__.
    *
    * @since 0.1.0
    */
  val LOCAL_TEMP_PATH: String = "localTempPath"

  /**
    * Constant for the option key __dfsTempPath__.
    *
    * @since 0.1.0
    */
  val DFS_TEMP_PATH: String = "dfsTempPath"

  /**
    * Constant for the option key __uploadFilePrefix__.
    *
    * @since 0.1.0
    */
  val UPLOAD_FILE_PREFIX: String = "uploadFilePrefix"

  /**
    * Constant for the option key __CLIENT_ID__.
    *
    * @since 0.1.0
    */
  val CLIENT_ID: String = "client_id"

  /**
    * Constant for the option key __client_secret__.
    *
    * @since 0.1.0
    */
  val CLIENT_SECRET: String = "client_secret"

  /**
    * Constant for the option key __grant_type__.
    *
    * @since 0.1.0
    */
  val GRANT_TYPE: String = "grant_type"

  /**
    * Constant for the option key __RESOURCE__.
    *
    * @since 0.1.0
    */
  val RESOURCE: String = "resource"

  /**
    * Constant for the option key __RESOURCE__.
    *
    * @since 0.1.0
    */
  val URI: String = "uri"

  /**
    * Constant for the option key __METHOD__.
    *
    * @since 0.1.0
    */
  val METHOD: String = "method"

  /**
    * Constant for the option key __DATA__.
    *
    * @since 0.1.0
    */
  val DATA: String = "data"

  /**
    * Constant for the option key __OUTPUT_PATH__.
    *
    * @since 0.1.0
    */
  val OUTPUT_PATH: String = "output_path"

  /**
    * Constant for the option key __TENANT_ID__.
    *
    * @since 0.1.0
    */
  val TENANT_ID: String = "tenant_id"

  /**
    * Constant for the option key __URL__.
    *
    * @since 0.1.0
    */
  val URL: String = "url"

  /**
    * Constant for the option key __SOURCE_PATH__.
    *
    * @since 0.1.0
    */
  val SOURCE_PATH: String = "source_path"

  /**
    * Constant for the option key __SOURCE_PATH__.
    *
    * @since 0.1.0
    */
  val FILE_NAME: String = "file_name"

  /**
    * Set of all the Spark File Transfer options above
    * that will be excluded from the overall options
    * set on the DataFrame API.
    *
    * @since 0.1.0
    */
  val excludeOptionsToDataFrame: Set[String] = Set(
    PROTOCOL,
    FILE_FORMAT,
    PATH,
    LOCAL_TEMP_PATH,
    DFS_TEMP_PATH,
    UPLOAD_FILE_PREFIX
  )
}
