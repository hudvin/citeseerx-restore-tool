//import java.lang.String
//import scala.Predef.String
//import scala.slick.driver.MySQLDriver.simple._
//import scala.slick.jdbc.meta.MTable
//
//
////trait TransactionSupport {
////
////  def withTransaction[T](f: => T): T = {
////
////    Database.forURL("jdbc:mysql://localhost:3306/citeseerx",
////      driver = "com.mysql.jdbc.Driver", user = "root", password = "azqwerty") withSession {
////      f
////    }
////  }
////
////}
////
////
////class Account(val id: Option[Int], val name: String, val email: String, val password: String, val isConfirmed: Boolean)
////
////class AccountService() extends TransactionSupport {
////
////  def addAccount(account: Account)(implicit session: Session): Unit = {
////    AccountTable.insert(account.id, account.name, account.email, account.password, account.isConfirmed)
////    println("insert")
////    //  }
////  }
////
////  def getAccountById(id: Int)(implicit session: Session): Unit = {
////    // Database.forURL("jdbc:h2:mem:test1", driver = "org.h2.Driver") withSession {
////    println("====")
////    Console println AccountTable.filter(_.id === id).list
////    println("====")
////    //  }
////  }
////
////  def getAccountByCredentials(email: String, password: String)(implicit session: Session): Unit = {
////    println("****")
////    Console println AccountTable.filter(a => a.email === email && a.password === password).list
////    println("****")
////  }
////
////
////}
//
//
///*
//  name
//  email
//  isConfirmed
//  id
//
// */
//object AccountTable extends Table[(Option[Int], String, String, String)]("records") {
//
//  def id = column[Int]("id", O.PrimaryKey, O.AutoInc)
//
//  def identifier = column[String]("identifier")
//
//  def datestamp = column[String]("datestamp")
//
//  def setSpec = column[String]("setSpec")
//
//
//  def title: String = _
//  def creator: String = _
//  def subject: String = _
//  def description: String = _
//  def contributor: String = _
//  def publisher: String = _
//  def date: String = _
//  def format: String = _
//  def identifier: String = _
//  def source: String = _
//  def language: String = _
//  def rights: String = _
//
//
//
//
//  def * = id.? ~ identifier ~ datestamp ~ setSpec
//
//
//
//
//}
//
//
//object Hi {
//
//
//  def main(args: Array[String]) = {
//
//    Database.forURL("jdbc:mysql://localhost:3306/citeseerx",
//      driver = "com.mysql.jdbc.Driver", user = "root", password = "azqwerty") withSession {
//      implicit session: Session =>
//      // The session is never named explicitly. It is bound to the current
//      // thread as the threadLocalSession that we imported
//      // Create the tables, including primary and foreign keys
//        if (!MTable.getTables.list.exists(_.name.name == AccountTable.tableName)) {
//          AccountTable.ddl.create
//        }
//
//
//        AccountTable.insert(None, "Vadym", "hudvin@gmail.com", "password")
//
//
//      //        // Insert some suppliers
//      //        Suppliers.insert(101, "Acme, Inc.", "99 Market Street", "Groundsville", "CA", "95199")
//      //        Suppliers.insert(49, "Superior Coffee", "1 Party Place", "Mendocino", "CA", "95460")
//      //        Suppliers.insert(150, "The High Ground", "100 Coffee Lane", "Meadows", "CA", "93966")
//      //
//      //        // Insert some coffees (using JDBC's batch insert feature, if supported by the DB)
//      //        Coffees.insertAll(
//      //          ("Colombian", 101, 7.99, 0, 0),
//      //          ("French_Roast", 49, 8.99, 0, 0),
//      //          ("Espresso", 150, 9.99, 0, 0),
//      //          ("Colombian_Decaf", 101, 8.99, 0, 0),
//      //          ("French_Roast_Decaf", 49, 9.99, 0, 0)
//      //        )
//      //
//      //        Query(Coffees) foreach {
//      //          case (name, supID, price, sales, total) =>
//      //            println("  " + name + "\t" + supID + "\t" + price + "\t" + sales + "\t" + total)
//      //        }
//      //
//      //        // Why not let the database do the string conversion and concatenation?
//      //        val q1 = for (c <- Coffees) // Coffees lifted automatically to a Query
//      //        yield ConstColumn("  ") ++ c.name ++ "\t" ++ c.supID.asColumnOf[String] ++
//      //            "\t" ++ c.price.asColumnOf[String] ++ "\t" ++ c.sales.asColumnOf[String] ++
//      //            "\t" ++ c.total.asColumnOf[String]
//      //        // The first string constant needs to be lifted manually to a ConstColumn
//      //        // so that the proper ++ operator is found
//      //        q1 foreach println
//      //
//      //        val q2 = for {
//      //          c <- Coffees if c.price < 9.0
//      //          s <- Suppliers if s.id === c.supID
//      //        } yield (c.name, s.name)
//      //        Console println q2.list()
//      //
//      //        val a = for {
//      //          t <- AccountTable
//      //        } yield t
//      //
//      //        Console println a.list()
//      //
//      //
//      //        println("Hi!")
//      //
//      //        val accountService = new AccountService
//      //        val account = new Account(None, "hudvin", "hudvin@gmail.com", "qwerty", true)
//      //        accountService.addAccount(account)
//      //        accountService.getAccountById(2)
//      //        accountService.getAccountByCredentials("hudvin@gmail.com", "qwerty")
//      //
//      //        val b = for {
//      //          t <- AccountTable
//      //        } yield t
//      //
//      //        Console println b.list()
//      //
//
//    }
//
//
//  }
//
//}
