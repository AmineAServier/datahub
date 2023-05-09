package com.github.servier.datahub.graph.util

/**
  * Supported file transfer protocols to upload/download content.
  *
  *  1. [[Protocol#graph graph]]
  *
  * @since 0.1.0
  */
object Protocol extends Enumeration {
  type Protocol = Value
  val graph = Value
}
