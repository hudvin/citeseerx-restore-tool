package com.nevilon.citeseerx.tool

import scala.collection.mutable
import scala.xml.pull.{EvText, EvElemEnd, EvElemStart, XMLEventReader}
import java.io.File


class Importer(filename:String,onNewRecord: Record => Unit) {


  def parse() {
    implicit def buf2Str(buff: mutable.StringBuilder): String = buff.toString
    val reader = new XMLEventReader(io.Source.fromFile(new File(filename)))
    val buffer: StringBuilder = new StringBuilder
    val path: collection.mutable.Stack[String] = new collection.mutable.Stack[String]
    var currentFieldName = ""
    var currentRecord: Record = null
    while (reader.hasNext) {
      val next = reader.next()
      next match {
        case EvElemStart(_, label, attr_, _) => {
          path.push(label)
          currentFieldName = label
          buffer.clear()
          path.slice(0, 1).toList match {
            case List("record") => {
              if (currentRecord != null) {
                //sorry
                onNewRecord(currentRecord)
              }
              currentRecord = new Record
            }
            case _ => {
            }
          }
        }
        case EvElemEnd(_, label) => {
          path.pop()
          path.toList match {
            case List("dc", "metadata", "record") => {
              currentFieldName match {
                case "relation" => {
                  currentRecord.metadata.dc.relations += buffer
                }
                case "title" => {
                  currentRecord.metadata.dc.title = buffer
                }
                case "creator" => {
                  currentRecord.metadata.dc.creator = buffer
                }
                case "subject" => {
                  currentRecord.metadata.dc.subject = buffer
                }
                case "description" => {
                  currentRecord.metadata.dc.description = buffer
                }
                case "contributor" => {
                  currentRecord.metadata.dc.contributor = buffer
                }
                case "publisher" => {
                  currentRecord.metadata.dc.publisher = buffer
                }
                case "date" => {
                  currentRecord.metadata.dc.date = buffer
                }
                case "format" => {
                  currentRecord.metadata.dc.format = buffer
                }
                case "identifier" => {
                  currentRecord.metadata.dc.identifier = buffer
                }
                case "source" => {
                  currentRecord.metadata.dc.source = buffer
                }
                case "language" => {
                  currentRecord.metadata.dc.language = buffer
                }
                case "rights" => {
                  currentRecord.metadata.dc.rights = buffer
                }
                case _ => {
                }
              }
            }
            case List("header", "record") => {
              currentFieldName match {
                case "identifier" => {
                  currentRecord.header.identifier = buffer
                }
                case "datestamp" => {
                  currentRecord.header.datestamp = buffer
                }
                case "setSpec" => {
                  currentRecord.header.setSpec = buffer
                }
              }
            }
            case _ => {}
          }
        }
        case EvText(text) => {
          buffer.append(text)
        }
        case _ => {}
      }
    }
  }

}
