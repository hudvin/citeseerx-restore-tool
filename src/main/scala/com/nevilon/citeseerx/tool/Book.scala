package com.nevilon.citeseerx.tool

;

import java.util.ArrayList;
import java.util.Date;
import java.util.List
import java.lang.String
import scala.collection.mutable.ListBuffer
;

/**
 * Book class stores book information, after parsing the xml
 *
 * @author Ganesh Tiwari
 */


class Record {

  var header: Header = new Header
  var metadata = new Metadata

}

class Header {

  var identifier: String = _
  var datestamp: String = _
  var setSpec: String = _

}


class Metadata {

  var dc: Dc = new Dc

}

class Dc {

  var title: String = _
  var creator: String = _
  var subject: String = _
  var description: String = _
  var contributor: String = _
  var publisher: String = _
  var date: String = _
  var format: String = _
  var identifier: String = _
  var source: String = _
  var language: String = _
  var rights: String = _
  var relations:ListBuffer[String] = new ListBuffer[String]


}
