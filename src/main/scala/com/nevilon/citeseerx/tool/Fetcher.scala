package com.nevilon.citeseerx.tool


import javax.activation.MimeType
import scala.Unit
import java.io.{IOException, FileNotFoundException, InputStream}
import java.net.{ConnectException, UnknownHostException, URL}


class EntityParams(val size: Long, val url: String, val mimeType: MimeType)

abstract class Fetcher(url: String) {

  def onException(ex: Exception): Unit

  def onHttpError(httError: Int): Unit

  def onDataStream(entityParams: EntityParams, is: InputStream): Unit

  private def buildEntityParams(contentType: String, url: String, contentLength: Long): EntityParams = {
    val mimeType = new MimeType(contentType)
    val entityParams = new EntityParams(contentLength, url, mimeType)
    entityParams
  }

  def load() {
    try {
      val encodedUrl = url
      println(encodedUrl)

      try {
        val urlAddr = new URL(url)
        val connection = urlAddr.openConnection()
        val contentLength = connection.getContentLength
        val contentType = connection.getContentType
        val contentStream = connection.getInputStream
        val entityParams = buildEntityParams(contentType, url, contentLength)
        //  onDataStream(entityParams, contentStream)
        contentStream.close()
      }
      catch {
        case e: FileNotFoundException => {
          onHttpError(404)
          println("not found")
        }
        case e: UnknownHostException => {
          onHttpError(-1)
          println("host not found " + url)
        }
        case e: ConnectException => {
          print(e.getMessage)
        }
        case e: IOException => {
          print(e.getMessage)
        }
        case e: Throwable => {
          println(e.getMessage)
        }
      }
    }
  }


}