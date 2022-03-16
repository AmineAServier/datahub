package com.github.servier.datahub.sharepoint.client

/**
  * Factory for remote file transfer client.
  *
  * The following classes extends this trait:
  *  - [[sharepoint Sharepoint]]
  *
  * @since 0.1.0
  */
trait BaseClient {

  /**
    * Downloads files from remote host.
    *
    * @param src Remote file/directory path.
    * @param dest Local directory path.
    * @return Returns unit of successful download.
    *
    * @since 0.1.0
    */
  def download(src: String, dest: String): Unit

}
