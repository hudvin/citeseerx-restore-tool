package com.nevilon.citeseerx.tool

import scala.xml.pull.{EvText, EvElemEnd, EvElemStart, XMLEventReader}
import java.io.{InputStream, File}
import scala.collection.mutable

import scala._
import scala.xml.pull.EvElemStart
import scala.collection
import scala.xml.pull.EvText
import scala.xml.pull.EvElemEnd
import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.commons.{MongoDBList, MongoDBObject}
import java.util.concurrent.{ArrayBlockingQueue, TimeUnit, BlockingQueue, ThreadPoolExecutor}








//class Downloader {
//
//  final val QUEUE_SIZE: Int = 25
//  final val POOL_SIZE: Int = 25
//  final val MAX_POOL_SIZE: Int = 25
//  final val KEEP_ALIVE_TIME: Int = 3000
//  private val blockingQueue: BlockingQueue[Runnable] = new ArrayBlockingQueue[Runnable](QUEUE_SIZE)
//  private val executor: DownloadThreadPoolExecutor = new DownloadThreadPoolExecutor(POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, blockingQueue)
//  init()
//
//  def init() {
//    blockingQueue.add()
//  }
//
//  def start() {
//
//  }
//
//  def stop() {
//
//  }
//
//  def getNextRecord() {
//
//  }
//
//  def updateRecord() {
//
//  }
//
//
//}




