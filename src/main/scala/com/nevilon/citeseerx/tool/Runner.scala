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
                  currentRecord.metadata.dc.relations += buffer.toString()
                }

                case "title" => {
                  currentRecord.metadata.dc.title = buffer.toString()
                }
                case "creator" => {
                  currentRecord.metadata.dc.creator = buffer.toString()
                }
                case "subject" => {
                  currentRecord.metadata.dc.subject = buffer.toString()
                }
                case "description" => {
                  currentRecord.metadata.dc.description = buffer.toString()
                }
                case "contributor" => {
                  currentRecord.metadata.dc.contributor = buffer.toString()
                }
                case "publisher" => {
                  currentRecord.metadata.dc.publisher = buffer.toString()
                }
                case "date" => {
                  currentRecord.metadata.dc.date = buffer.toString()
                }
                case "format" => {
                  currentRecord.metadata.dc.format = buffer.toString()
                }
                case "identifier" => {
                  currentRecord.metadata.dc.identifier = buffer.toString()
                }
                case "source" => {
                  currentRecord.metadata.dc.source = buffer.toString()
                }
                case "language" => {
                  currentRecord.metadata.dc.language = buffer.toString()
                }
                case "rights" => {
                  currentRecord.metadata.dc.rights = buffer.toString()
                }
                case _ => {
                }

              }
            }
            case List("header", "record") => {
              currentFieldName match {
                case "identifier" => {
                  currentRecord.header.identifier = buffer.toString()
                }
                case "datestamp" => {
                  currentRecord.header.datestamp = buffer.toString()
                }
                case "setSpec" => {
                  currentRecord.header.setSpec = buffer.toString()
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
