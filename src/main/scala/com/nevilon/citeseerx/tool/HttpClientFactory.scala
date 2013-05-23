package com.nevilon.citeseerx.tool

//import org.apache.http.impl.client.{DefaultHttpRequestRetryHandler, DefaultHttpClient}
//import org.apache.http.impl.conn.PoolingClientConnectionManager
//import org.apache.http.conn.ConnectionKeepAliveStrategy
//import org.apache.http.HttpResponse
//import org.apache.http.protocol.{HTTP, HttpContext}
//import org.apache.http.message.BasicHeaderElementIterator
//import org.apache.http.conn.scheme.{PlainSocketFactory, Scheme, SchemeRegistry}
//import org.apache.http.conn.ssl.{TrustStrategy, SSLSocketFactory}
//import java.security.cert
//import org.apache.http.params.CoreProtocolPNames
//import org.apache.http.client.HttpRequestRetryHandler
//import java.io.IOException


//object HttpClientFactory {
//
//
//  private val stubSSLSocketFactory = new SSLSocketFactory(new TrustStrategy() {
//
//    def isTrusted(chain: Array[cert.X509Certificate], authType: String) = true
//  }, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
//
//
//  def buildHttpClient(threadsTotal: Int, threadsPerHost: Int, userAgent: String): DefaultHttpClient = {
//
//
//    val cm = new PoolingClientConnectionManager(buildSchemeRegistry())
//    cm.setMaxTotal(threadsTotal)
//    cm.setDefaultMaxPerRoute(threadsPerHost)
//    val httpClient = new DefaultHttpClient(cm)
//    httpClient.getParams().setParameter(CoreProtocolPNames.USER_AGENT, userAgent)
//
//
//
//    httpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(1, false))
//    httpClient
//  }
//
//  private def buildSchemeRegistry(): SchemeRegistry = {
//    val schemeRegistry = new SchemeRegistry()
//    schemeRegistry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory))
//    schemeRegistry.register(new Scheme("https", 443, stubSSLSocketFactory))
//    schemeRegistry
//  }
//
//
//
//}

