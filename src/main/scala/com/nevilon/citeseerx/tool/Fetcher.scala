package com.nevilon.citeseerx.tool


import javax.activation.MimeType
import scala.Unit
import org.eclipse.jetty.client.HttpClient
import java.io.{IOException, FileNotFoundException, InputStream}
import org.eclipse.jetty.client.util.InputStreamResponseListener
import java.util.concurrent.TimeUnit
import org.apache.commons.io.IOUtils
import org.eclipse.jetty.http.HttpHeader
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
        case e:Throwable=>{
          println(e.getMessage)
        }
      }


      //      val client = new HttpClient()
      //      client.start()
      //      val listener = new InputStreamResponseListener()
      //      client.newRequest(encodedUrl).send(listener)
      //      val response = listener.get(5, TimeUnit.SECONDS)
      //      if (response.getStatus() == 200) {
      //        val stream = listener.getInputStream()
      //        val contentType = response.getHeaders.getField(HttpHeader.CONTENT_TYPE).getValue
      //        onDataStream(buildEntityParams(contentType, url, response.getHeaders.getField(HttpHeader.CONTENT_LENGTH).getLongValue),
      //          stream)
      //        stream.close()
      //      } else {
      //        onHttpError(response.getStatus)
      //      }


      //      val httpGet = new HttpGet(encodedUrl)
      //      httpGet.setHeader("Connection", "close");
      //
      //      //
      //
      //
      //      var response: HttpResponse = null
      //      try {
      //        response = httpClient.execute(httpGet, new BasicHttpContext()) //what is context?
      //        val statusCode = response.getStatusLine.getStatusCode
      //        if (statusCode == HttpStatus.SC_OK) {
      //          val entity = response.getEntity
      //          val entityParams = buildEntityParams(entity, url)
      //          EntityUtils.consume(entity)
      //          entity.getContent.close()
      //          //onDataStream(entityParams, entity, url)
      //        } else {
      //          onHttpError(statusCode)
      //        }
      //      } catch {
      //        case e: Exception => {
      //          // httpGet.abort()
      //          httpGet.releaseConnection()
      //          onException(e)
      //        }
      //      }
      //      finally {
      //        if (response != null) {
      //          //        assert(response != null)
      //          //        assert(response.getEntity != null)
      //          //        assert(response.getEntity.getContent != null)
      //          //        response.getEntity.getContent.close()
      //        }
      //
      //
      //        //  httpGet.abort()
      //        httpGet.releaseConnection()
      //
      //      }
      //
      //
      //    } catch {
      //      case t: Throwable => {
      //        println("-------")
      //        // t.printStackTrace()
      //      }
      //    }

    }
  }


}