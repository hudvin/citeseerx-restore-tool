package com.nevilon.citeseerx.tool

import java.util.concurrent._
import com.mongodb.BasicDBObject
import java.lang.Thread.UncaughtExceptionHandler
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

    val QUEUE_SIZE: Int = 250
    val POOL_SIZE: Int = 250
    val MAX_POOL_SIZE: Int = 350
    val KEEP_ALIVE_TIME: Long = 10000L
    val blockingQueue: LinkedBlockingQueue[Runnable] = new LinkedBlockingQueue[Runnable](QUEUE_SIZE)


    val executor: DownloadThreadPoolExecutor = new DownloadThreadPoolExecutor(
      POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, blockingQueue)

    executor.prestartAllCoreThreads()

    var counter = 0;
    val t = new Thread() {

      override def run() {
        val it = mongoRecordSaver.getRecords()
        it.foreach(dbObj => {
          val record = Transformer.db2object(dbObj)
          val source = record.metadata.dc.source
          val worker = new Thread() {

            override def run() {
              //null source

              val fetcher = new Fetcher(source) {
                def onException(ex: Exception) {
                  record.status.httpStatus = "-1"

                }

                def onHttpError(httError: Int) {
                  record.status.httpStatus = "-1"
                  println("error " + httError)
                }

                def onDataStream(entityParams: EntityParams, is: InputStream) {
                  mongoStorage.saveStream(is, entityParams.url, entityParams.mimeType.toString) match {
                    case None => {}
                    case Some(gfsId) => {
                      record.status.gfsId = gfsId
                    }
                  }
                  record.status.httpStatus = "200"
                  println(counter)
                  counter += 1
                }
              }
              fetcher.load()
              record.status.isProcessed = true
              mongoRecordSaver.updateRecord(record)
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
