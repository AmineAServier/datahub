package com.github.servier.datahub.sharepoint

import com.github.servier.datahub.sharepoint.util.{FileTransferOptions, Protocol}

/**
  * Package object for common methods to access client classes.
  *
  * @since 0.1.0
  */
package object client {

  /**
    * Creates a ''client'' instance to the remote host for file transfer
    * for the type of the
    * [[Protocol.Value Protocol]]
    * value set in the options.
    *
    * @param options Options passed by the user on the
    *                '''DataFrameReader''' or '''DataFrameWriter''' API.
    * @return A file transfer client instance.
    * @see [[sharepoint Sharepoint]]
    * @since 0.1.0
    */
  def sharepointClient(
      options: FileTransferOptions
  ): Sharepoint = {
    options.protocol match {
      case Protocol.sharepoint => new Sharepoint(options)
      case x @ _ =>
        throw new NotImplementedError(
          s"Support ${x.toString} client is not yet implemented !!"
        )
    }
  }
}
