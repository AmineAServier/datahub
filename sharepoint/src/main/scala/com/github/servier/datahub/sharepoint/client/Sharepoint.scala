package com.github.servier.datahub.sharepoint.client

import com.github.servier.datahub.sharepoint.util.FileTransferOptions
import org.apache.spark.internal.Logging
import play.api.libs.json.Json
import scalaj.http.{Http, HttpRequest, HttpResponse}

import java.io
import java.io.FileOutputStream
import scala.util.{Failure, Success, Try}

/**
  * The `Sharepoint` class provides `upload` and `download` methods
  * to transfer files to/from remote host via '''Sharepoint'''.
  *
  * @constructor Create a new `Sharepoint` by specifying their `options`.
  * @param options Options passed by the user on the
  *                '''DataFrameReader''' or '''DataFrameWriter''' API.
  * @since 0.1.0
  */
class Sharepoint(options: FileTransferOptions) extends Logging {

  val parametersAuthentification: Map[String, String] =
    Map(
      "grant_type" -> options.grant_type.get,
      "resource" -> options.resource.get,
      "client_id" -> options.client_id.get,
      "client_secret" -> options.client_secret.get,
      "tenant_id" -> options.tenant_id.get,
      "resource" -> options.resource.get
    )

  val parametersData: Map[String, String] =
    Map(
      "url" -> options.url.get,
      "source_path" -> options.source_path.get,
      "file_name" -> options.file_name.get,
      "localTempPath" -> options.localTempPath
    )

  private def getBearer: String = {
    val msAccessURI =
      s"https://accounts.accesscontrol.windows.net/${parametersAuthentification("tenant_id")}/tokens/OAuth/2"

    val bearerToken: Try[String] = Try {
      val bearerRequest: HttpRequest = Http(msAccessURI).postForm {
        parametersAuthentification.toSeq
      }
      Json.parse(bearerRequest.asString.body)("access_token").toString()
    }
    bearerToken match {
      case Success(validBearerToken) => validBearerToken
      case Failure(_) =>
        throw new RuntimeException(
          "Invalid Bearer token. Please check your credentials."
        )
    }
  }

  private def getBodyStream(
      httpReq: scalaj.http.HttpRequest
  ): HttpResponse[Unit] = {
    httpReq.execute { is =>
      val buffer: Array[Byte] = Array.ofDim[Byte](1024)
      val temporaryFolder: String =
        s"${parametersData("localTempPath")}/${parametersData("file_name")}"
      val fos = new FileOutputStream(
        temporaryFolder
      )
      var read: Int = 0
      while (read >= 0) {
        read = is.read(buffer)
        if (read > 0) {
          fos.write(buffer, 0, read)
        }
      }
      fos.close()
    }
  }

  private def getServerRelativeUrl(
      httpReq: scalaj.http.HttpRequest
  ): String = {
    val jsonResponse: HttpResponse[String] = httpReq.asString
    val serverRelativeUrl: Try[String] = Try {
      Json.parse(jsonResponse.body)("value")(0)("ServerRelativeUrl").toString()
    }
    serverRelativeUrl match {
      case Success(validServerRelativeUrl) => validServerRelativeUrl
      case Failure(_) =>
        throw new RuntimeException(
          "Invalid URL. Please check your path."
        )
    }
  }
  def uploadFile: io.Serializable = {
    val headers = Seq(
      "Authorization" -> s"Bearer ${getBearer.replace("\"", "")}"
    )
    val uri = s"${parametersData("url")}_api/web/GetFileByServerRelativeUrl('${parametersData("source_path")}')/" + "$value"
    val documentRequest: HttpRequest = Http(uri).headers(headers)
    val file: Try[HttpResponse[Unit]] = Try {
      getBodyStream(documentRequest)
    }
    file.get.code match {
      case 200 => file.get
      case _ => throw new RuntimeException(
        s"Could not downland the file. Sharepoint API responded HTTP code : ${file.get.code}"
      )
    }
    /*
    file.getOrElse(
      s"Could not downland the file. Sharepoint API responded HTTP code : ${file.get.code}"
    )*/

  }

  def uploadLastFile: io.Serializable = {
    val headers = Seq(
      "Authorization" -> s"Bearer ${getBearer.replace("\"", "")}",
      "Accept" -> "application/json"
    )
    val folder_url = s"${parametersData("url")}_api/web/GetFolderByServerRelativeUrl('${parametersData("source_path")}')/Files" + "?$orderby=TimeLastModified%20desc" + "&$top=1" + "&$select=ServerRelativeUrl"
    val folderRequest: HttpRequest = Http(folder_url).headers(headers)
    val ServerRelativeUrl = getServerRelativeUrl(folderRequest).replace(" ", "%20")

    val uri = s"${parametersData("url")}_api/web/GetFileByServerRelativeUrl('${ServerRelativeUrl.replace("\"", "")}')/" + "$value"

    val documentRequest: HttpRequest = Http(uri).headers(headers)
    val file: Try[HttpResponse[Unit]] = Try {
      getBodyStream(documentRequest)
    }

    file.get.code match {
      case 200 => file.get
      case _ => throw new RuntimeException(
        s"Could not downland the file. Sharepoint API responded HTTP code : ${file.get.code}"
      )
    }
  }
}
