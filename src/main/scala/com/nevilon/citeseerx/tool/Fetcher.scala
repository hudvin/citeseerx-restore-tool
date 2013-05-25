package com.nevilon.citeseerx.tool


import javax.activation.MimeType
import scala.Unit
import java.io.{BufferedInputStream, IOException, FileNotFoundException, InputStream}
import java.net.{ConnectException, UnknownHostException, URL}


class EntityParams(val size: Long, val url: String, val mimeType: MimeType)

abstract class Fetcher(url: String) {

  def onException(message:String): Unit

  def onDataStream(entityParams: EntityParams, is: InputStream): Unit

  private def buildEntityParams(contentType: String, url: String, contentLength: Long): EntityParams = {
    val mimeType = new MimeType(contentType)
    val entityParams = new EntityParams(contentLength, url, mimeType)
    entityParams
  }

  def load() {
    try {
      val encodedUrl = url
      try {
        System.setProperty("java.net.preferIPv4Stack", "true")
        System.setProperty("http.keepAlive", "false")

        val urlAddr = new URL(url)
        val connection = urlAddr.openConnection()
        connection.setUseCaches(false)
        connection.setRequestProperty("Connection", "close")

        val contentLength = connection.getContentLength
        val contentType = connection.getContentType
        val contentStream = connection.getInputStream
        val entityParams = buildEntityParams(contentType, url, contentLength)
        onDataStream(entityParams, new BufferedInputStream(contentStream))
        contentStream.close()
      }
      catch {
        case e: Throwable => {
          onException(e.toString)
        }
      }
    }
  }


}