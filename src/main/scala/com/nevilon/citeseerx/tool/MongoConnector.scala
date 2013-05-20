package com.nevilon.citeseerx.tool

import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.gridfs.GridFS

class MongoConnector {

  private val mongoClient = MongoClient()
  private val mongoDB = mongoClient("citeseerx")
  val meta = mongoDB("meta")
  val gridfs = GridFS(mongoDB)


}
