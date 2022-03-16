package com.github.servier.datahub.sharepoint.util

/**
  * Supported file transfer protocols to upload/download content.
  *
  *  1. [[Protocol#sharepoint sharepoint]]
  *
  * @since 0.1.0
  */
object Protocol extends Enumeration {
  type Protocol = Value
  val sharepoint = Value
}
