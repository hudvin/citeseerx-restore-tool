package com.nevilon.citeseerx.tool

import com.mongodb.casbah.{MongoCursor, MongoCollection}
import com.mongodb.casbah.commons.{MongoDBList, MongoDBObject}

import scala.language.dynamics
import com.mongodb.{BasicDBList, BasicDBObject, DBObject}
import scala.collection.mutable.ListBuffer
import org.bson.types.ObjectId


class DynamicMongoDBObject(dbObject: DBObject) extends Dynamic {

  def selectDynamic(fieldName: String): DynamicMongoDBObject = {
    val fieldItem = dbObject.get(fieldName)
    if (fieldItem.isInstanceOf[String]) {
      new StringDynamicMongoDBObject(fieldItem.asInstanceOf[String])
    } else {
      new DynamicMongoDBObject(fieldItem.asInstanceOf[DBObject])
    }
  }

  override def toString = {
    if (dbObject != null) dbObject.toString else ""
  }

}


class StringDynamicMongoDBObject(value: String) extends DynamicMongoDBObject(null) {

  override def selectDynamic(fieldName: String): DynamicMongoDBObject = {
    throw new RuntimeException
  }


  override def toString = {
    value
  }

}


object Transformer {

  implicit final def mongo2dynamic(dbObj: DBObject): DynamicMongoDBObject = new DynamicMongoDBObject(dbObj)

  implicit final def dyn2String(dyn: DynamicMongoDBObject): String = dyn.toString

  def db2object(dbObject: DBObject): Record = {
    val record = new Record
    val dyn = mongo2dynamic(dbObject)
    //sorry
    record.id = dbObject.get("_id").toString
    record.header.datestamp = dyn.header.datestamp
    record.header.identifier = dyn.header.identifier
    record.header.setSpec = dyn.header.setSpec
    record.metadata.dc.contributor = dyn.metadata.dc.contributor
    record.metadata.dc.creator = dyn.metadata.dc.creator
    record.metadata.dc.date = dyn.metadata.dc.date
    record.metadata.dc.description = dyn.metadata.dc.description
    record.metadata.dc.format = dyn.metadata.dc.format
    record.metadata.dc.identifier = dyn.metadata.dc.identifier
    record.metadata.dc.language = dyn.metadata.dc.language
    record.metadata.dc.publisher = dyn.metadata.dc.publisher
    record.metadata.dc.rights = dyn.metadata.dc.rights
    record.metadata.dc.source = dyn.metadata.dc.source
    record.metadata.dc.subject = dyn.metadata.dc.subject
    record.metadata.dc.title = dyn.metadata.dc.title

    val statusObj = dbObject.get("status").asInstanceOf[DBObject]
    val idObj = dbObject.get("gfsId")
    if (idObj != null) {
      record.status.gfsId = idObj.asInstanceOf[ObjectId].toString

    }

    record.status.httpStatus = dyn.status.httpStatus
    record.status.isProcessed = (dyn.status.isProcessed.toString).toBoolean

    //sorry
    val relations = dbObject.get("metadata").asInstanceOf[DBObject]
      .get("dc").asInstanceOf[DBObject]
      .get("relations").asInstanceOf[BasicDBList]
    new MongoDBList(relations).foreach(item => record.metadata.dc.relations += item.toString)
    record
  }

  def object2db(record: Record): DBObject = {
    val recordObj = MongoDBObject(
      "status" -> MongoDBObject(
        "gfsId" -> record.status.gfsId,
        "httpStatus" -> record.status.httpStatus,
        "isProcessed" -> record.status.isProcessed.toString
      ),
      "header" -> MongoDBObject(
        "identifier" -> record.header.identifier,
        "datestamp" -> record.header.datestamp,
        "setSpec" -> record.header.setSpec
      ),
      "metadata" -> MongoDBObject("dc" -> MongoDBObject(
        "title" -> record.metadata.dc.title,
        "creator" -> record.metadata.dc.creator,
        "subject" -> record.metadata.dc.subject,
        "description" -> record.metadata.dc.description,
        "contributor" -> record.metadata.dc.contributor,
        "publisher" -> record.metadata.dc.publisher,
        "date" -> record.metadata.dc.date,
        "format" -> record.metadata.dc.format,
        "identifier" -> record.metadata.dc.identifier,
        "source" -> record.metadata.dc.source,
        "language" -> record.metadata.dc.language,
        "rights" -> record.metadata.dc.rights,
        "relations" -> (record.metadata.dc.relations)
      )
      )
    )
    import com.mongodb.casbah.commons.Implicits._
    recordObj.asDBObject
  }

}

class MongoRecordSaver(collection: MongoCollection) {

  def updateStatus() {

  }


  //  def getRecord() {
  //    val dbObj = collection.find({})
  //    val record = new Record
  //    record.header.identifier = dbObj.g
  //    "datestamp" -> record.header.datestamp,
  //    "setSpec" -> record.header.setSpec
  //
  //    record.header.datestamp
  //
  //
  //  }


  def saveRecord(record: Record) {
    collection.save(Transformer.object2db(record))
  }


  def updateRecord(record: Record) {
    collection.update(MongoDBObject("_id" -> new ObjectId(record.id)), Transformer.object2db(record))
  }

  // db.meta.find({},{'metadata.dc.source':1});
  def getRecords(): MongoCursor = {
    collection.find(MongoDBObject("status.isProcessed" -> "false"))
  }

}
