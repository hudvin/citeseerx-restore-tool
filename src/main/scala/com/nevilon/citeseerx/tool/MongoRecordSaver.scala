package com.nevilon.citeseerx.tool

import com.mongodb.casbah.{MongoCursor, MongoCollection}
import com.mongodb.casbah.commons.MongoDBObject


class MongoRecordSaver(collection: MongoCollection) {

  def saveRecord(record: Record) {
    val recordObj = MongoDBObject("header" -> MongoDBObject(
      "status" -> MongoDBObject(
        "gfsId" -> "",
        "is_downloaded" -> "",
        "http_error" -> ""

      ),
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
    collection.save(recordObj)
  }

  // db.meta.find({},{'metadata.dc.source':1});
  def getRecord(): MongoCursor = {
    collection.iterator
  }

}
