package com.nevilon.citeseerx.tool

import com.mongodb.casbah.MongoClient
import com.mongodb.casbah.gridfs.GridFS

class MongoConnector(dbName: String, dbColl: String) {

  private val mongoClient = MongoClient()
  private val mongoDB = mongoClient(dbName)
  val meta = mongoDB(dbColl)
  val gridfs = GridFS(mongoDB)


}
