package com.github.servier.datahub

import com.github.servier.datahub.graph.util.{FileTransferOptions, Protocol}
import org.apache.spark.sql.{DataFrame, DataFrameReader, DataFrameWriter}

package object graph {

  /**
    * Adds methods for each supported [[Protocol Protocol]],
    * to DataFrameReader that allows you to read remote files via the selected protocol.
    *
    * @constructor Provides implicits to `DataFrameReader` for supported protocols.
    * @param reader a DataFrameReader
    * @since 0.2.0
    */
  implicit class ProtocolDataFrameReader(reader: DataFrameReader) {
    def graph: String => DataFrame = {
      reader
        .format("com.github.servier.datahub.graph")
        .option(FileTransferOptions.PROTOCOL, Protocol.graph.toString)
        .load
    }
  }

  /**
    * Adds methods for each supported [[Protocol Protocol]],
    * to DataFrameWriter that allows you to write remote files via the selected protocol.
    *
    * @constructor Provides implicits to `DataFrameWriter` for supported protocols.
    * @param writer a DataFrameWriter
    *
    * @since 0.2.0
    */
  implicit class ProtocolDataFrameWriter[T](writer: DataFrameWriter[T]) {
    def graph: String => Unit = {
      writer
        .format("com.github.servier.datahub.graph")
        .option(FileTransferOptions.PROTOCOL, Protocol.graph.toString)
        .save
    }
  }

}
