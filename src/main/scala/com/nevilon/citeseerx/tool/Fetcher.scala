package com.nevilon.citeseerx.tool


import org.apache.http.client.HttpClient
import org.apache.http.{HttpStatus, HttpEntity}
import org.apache.http.client.methods.HttpGet
import org.apache.http.protocol.BasicHttpContext
import javax.activation.MimeType
import scala.Unit


class EntityParams(val size: Long, val url: String, val mimeType: MimeType)

 abstract class Fetcher(url: String, httpClient: HttpClient) {

  def onException(ex: Exception): Unit

  def onHttpError(httError: Int): Unit

  def onDataStream(entityParams: EntityParams, httpEntity: HttpEntity, url: String): Unit

  private def buildEntityParams(httpEntity: HttpEntity, url: String): EntityParams = {
    val mimeType = new MimeType(httpEntity.getContentType.getValue)
    val entityParams = new EntityParams(httpEntity.getContentLength, url, mimeType)
    entityParams
  }


  def load() {
    val encodedUrl = url
    val httpGet = new HttpGet(encodedUrl)
    try {
      val response = httpClient.execute(httpGet, new BasicHttpContext()) //what is context?
      val statusCode = response.getStatusLine.getStatusCode
      if (statusCode == HttpStatus.SC_OK) {
        val entity = response.getEntity
        val entityParams = buildEntityParams(entity, url)
        onDataStream(entityParams, entity, url)
      } else {
        onHttpError(statusCode)
      }
    } catch {
      case e: Exception => {
        httpGet.abort()
        onException(e)
      }
    }
    finally {
      httpGet.abort()
    }
  }
}