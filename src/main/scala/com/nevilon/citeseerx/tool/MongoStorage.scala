package com.nevilon.citeseerx.tool

import com.mongodb.casbah.gridfs.GridFS
import java.io.InputStream
import org.bson.types.ObjectId

class MongoStorage(gridfs: GridFS) {


  def saveStream(is: InputStream, url: String, contentType: String, urlId: String): Option[String] = {
    gridfs(is) {
      file =>
        file.contentType = contentType
        file.filename = url
        file.put("urlId", urlId)
    }
    match {
      case None => None //throw exception or None?
      case Some(objectId) => {
        Some(objectId.asInstanceOf[ObjectId].toString)
      }
    }
  }

}
