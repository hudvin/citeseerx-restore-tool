package com.nevilon.citeseerx.tool

import java.util.concurrent._
import java.io.InputStream
import java.util.Properties

object Controller {

  private val properties = new Properties()
  properties.load(getClass.getResourceAsStream("/tool.properties"))
  private val POOL_SIZE_VALUE = properties.getProperty("pool_size").toInt
  private val DB_NAME = properties.getProperty("db_name")
  private val DB_COLL = properties.getProperty("db_coll")


  private val mongoConnector = new MongoConnector(DB_NAME, DB_COLL)
  private val mongoRecordSaver = new MongoRecordSaver(mongoConnector.meta)
  private val mongoStorage = new MongoStorage(mongoConnector.gridfs)


  def main(args: Array[String]) {
    if (args.size == 2 && args(0) == "--import") {
      val pathToFile = args(1)
      importData(pathToFile)
    } else if (args.contains("--download") && args.size == 1) {
      downloadData()
    } else {
      println("wrong arguments")
    }

    def importData(pathToFile: String) {
      var counter = 0
      val importer = new Importer(pathToFile, (record: Record) => {
        mongoRecordSaver.saveRecord(record)
        counter += 1
        println("insert record " + counter)
      })
      importer.parse()
    }

    def downloadData() {
      val properties = new Properties()
      properties.load(getClass.getResourceAsStream("/tool.properties"))

      val QUEUE_SIZE: Int = POOL_SIZE_VALUE
      val POOL_SIZE: Int = POOL_SIZE_VALUE
      val MAX_POOL_SIZE: Int = POOL_SIZE_VALUE
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

                val fetcher = new Fetcher(source) {
                  def onException(message:String) {
                    record.status.httpStatus = message
                  }


                  def onDataStream(entityParams: EntityParams, is: InputStream) {
                    mongoStorage.saveStream(is, entityParams.url, entityParams.mimeType.toString) match {
                      case None => {}
                      case Some(gfsId) => {
                        record.status.gfsId = gfsId
                      }
                    }
                    record.status.httpStatus = "200"
                    counter += 1
                  }
                }
                fetcher.load()
                println("url: " + source + " http status: " + record.status.httpStatus + " counter: " + counter)
                record.status.isProcessed = true
                mongoRecordSaver.updateRecord(record)
              }
            }
            blockingQueue.put(worker)
          })
          executor.shutdown()
          println("end")
        }
      }
      t.start()
    }

  }
}