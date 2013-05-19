package com.nevilon.citeseerx.tool

import scala.xml.pull.{EvText, EvElemEnd, EvElemStart, XMLEventReader}
import java.io.File
import scala.collection.mutable

import scala._
import scala.xml.pull.EvElemStart
import scala.collection
import scala.xml.pull.EvText
import scala.xml.pull.EvElemEnd

/**
 * Created with IntelliJ IDEA.
 * User: hudvin
 * Date: 5/19/13
 * Time: 4:45 PM
 * To change this template use File | Settings | File Templates.
 */
object Runner {

  def main(args: Array[String]) {
    println("hello")
    parse()
  }

  def parse() {
    implicit def buf2Str(buff:mutable.StringBuilder): String = buff.toString

    val filename = "/tmp/ddd/oai_dc1.dump"
    val reader = new XMLEventReader(io.Source.fromFile(new File(filename)))
    val buffer: StringBuilder = new StringBuilder
    val path: collection.mutable.Stack[String] = new collection.mutable.Stack[String]
    var currentFieldName = ""
    var records = new collection.mutable.ListBuffer[Record]
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
              currentRecord = new Record
              records += currentRecord
              println(records.size)
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
