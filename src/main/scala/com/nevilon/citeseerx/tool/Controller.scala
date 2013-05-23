package com.nevilon.citeseerx.tool

import java.util.concurrent._
import com.mongodb.BasicDBObject
import java.lang.Thread.UncaughtExceptionHandler
import org.eclipse.jetty.client.HttpClient
import org.eclipse.jetty.client.util.InputStreamResponseListener
import org.apache.commons.io.IOUtils
import java.io.InputStream

object Controller {

  private val mongoConnector = new MongoConnector
  private val mongoRecordSaver = new MongoRecordSaver(mongoConnector.meta)


  def main(args: Array[String]) {
    args.toList match {
      case Nil => {
        println("no params")
      }
      case List("import") => {
        println("importing")
        importData()
      }
      case List("download") => {
        println("downloading")
        downloadData()
      }

    }
  }

  def importData() {
    val filename = "/tmp/ddd/bigfile"
    var counter = 0
    val importer = new Importer(filename, (record: Record) => {
      mongoRecordSaver.saveRecord(record)
      counter += 1
      println("insert record " + counter)
    })
    importer.parse()
  }

  def downloadData() {
    val mongoStorage = new MongoStorage(mongoConnector.gridfs)

    val QUEUE_SIZE: Int = 100
    val POOL_SIZE: Int = 100
    val MAX_POOL_SIZE: Int = 250
    val KEEP_ALIVE_TIME: Long = 0L
    val blockingQueue: LinkedBlockingQueue[Runnable] = new LinkedBlockingQueue[Runnable](QUEUE_SIZE)


    val executor: DownloadThreadPoolExecutor = new DownloadThreadPoolExecutor(
      POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, blockingQueue)

    executor.prestartAllCoreThreads()




        var counter = 0;
    //
    //
    //    val it = mongoRecordSaver.getRecord()
    //    it.foreach(dbObj => {
    //      val source = dbObj.get("metadata").asInstanceOf[BasicDBObject].
    //        get("dc").asInstanceOf[BasicDBObject]
    //        .get("source").asInstanceOf[String]
    //
    //      //  println(source)
    //
    //      val worker = new Thread() {
    //
    //
    //        setUncaughtExceptionHandler(new UncaughtExceptionHandler {
    //          def uncaughtException(t: Thread, e: Throwable) {
    //            println("oops")
    //          }
    //        })
    //
    //        override def run() {
    //          if (source == null) {
    //            setName("null")
    //          }
    //          else setName(source)
    //          val httpClient = new DefaultHttpClient(cm)
    //
    //          httpClient.getConnectionManager.closeExpiredConnections()
    //          httpClient.getParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000)
    //
    //          //              HttpClientFactory.buildHttpClient(POOL_SIZE, POOL_SIZE, "nemo")
    //
    //          val fetcher = new Fetcher(source, httpClient) {
    //            def onException(ex: Exception) {}
    //
    //            def onHttpError(httError: Int) {
    //              println("error " + httError)
    //            }
    //
    //            def onDataStream(entityParams: EntityParams, httpEntity: HttpEntity, url: String) {
    //              mongoStorage.saveStream(httpEntity.getContent, url, entityParams.mimeType.toString, "")
    //              println(counter)
    //              counter += 1
    //
    //            }
    //          }
    //          fetcher.load()
    //          //httpClient.getConnectionManager.shutdown()
    //        }
    //      }
    //
    //      println("put ")
    //      executor.purge()
    //      blockingQueue.put(worker)
    //
    //    })
    //    println("end!!!!!!!!")

    val t = new Thread() {

      override def run() {
        val it = mongoRecordSaver.getRecord()
        it.foreach(dbObj => {
          val source = dbObj.get("metadata").asInstanceOf[BasicDBObject].
            get("dc").asInstanceOf[BasicDBObject]
            .get("source").asInstanceOf[String]

          //  println(source)

          val worker = new Thread() {


            setUncaughtExceptionHandler(new UncaughtExceptionHandler {
              def uncaughtException(t: Thread, e: Throwable) {
                println("oops")
              }
            })

            override def run() {
              if (source == null) {
                setName("null")
              }
              else setName(source)


              val fetcher = new Fetcher(source) {
                def onException(ex: Exception) {}

                def onHttpError(httError: Int) {
                  println("error " + httError)
                }

                def onDataStream(entityParams: EntityParams, is: InputStream) {
                  mongoStorage.saveStream(is, entityParams.url, entityParams.mimeType.toString, "")
                  println(counter)
                  counter += 1

                }
              }
              fetcher.load()
              //httpClient.getConnectionManager.shutdown()
            }
          }

          println("put ")
          executor.purge()
          blockingQueue.put(worker)

        })
        println("end!!!!!!!!")
      }

    }

    t.start()
    // executor.shutdown()

  }

}
