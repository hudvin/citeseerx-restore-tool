package com.nevilon.citeseerx.tool

import java.util.concurrent.{TimeUnit, BlockingQueue, ArrayBlockingQueue}
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.BasicDBObject
import org.apache.http.HttpEntity

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
    val filename = "/tmp/ddd/oai_dc1.dump"
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

    val QUEUE_SIZE: Int = 150
    val POOL_SIZE: Int = 150
    val MAX_POOL_SIZE: Int = 150
    val KEEP_ALIVE_TIME: Int = 30000
    val blockingQueue: BlockingQueue[Runnable] = new ArrayBlockingQueue[Runnable](QUEUE_SIZE)
    val executor: DownloadThreadPoolExecutor = new DownloadThreadPoolExecutor(
      POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, blockingQueue)

    executor.prestartAllCoreThreads()


    var counter = 0;

    val t = new Thread() {

      override def run() {
        val it = mongoRecordSaver.getRecord()
        it.foreach(dbObj => {
          val source = dbObj.get("metadata").asInstanceOf[BasicDBObject].
            get("dc").asInstanceOf[BasicDBObject]
            .get("source").asInstanceOf[String]

        //  println(source)

          val worker = new Thread() {

            override def run() {
              val httpClient = HttpClientFactory.buildHttpClient(POOL_SIZE, POOL_SIZE, "nemo")

              val fetcher = new Fetcher(source, httpClient) {
                def onException(ex: Exception) {}

                def onHttpError(httError: Int) {}

                def onDataStream(entityParams: EntityParams, httpEntity: HttpEntity, url: String) {
                  mongoStorage.saveStream(httpEntity.getContent, url, entityParams.mimeType.toString, "")
                  println(counter)
                  counter+=1

                }
              }
              fetcher.load()
              httpClient.getConnectionManager.shutdown()
            }
          }


          blockingQueue.put(worker)

        })
      }
    }

    t.start();
  }

}
