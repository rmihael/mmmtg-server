/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/15/13
 * Time: 7:04 PM
 */

package com.grumpycats.mmmtg.models

import scala.language.postfixOps

import anorm._
import anorm.SqlParser._
import org.scala_tools.time.Imports._

trait PricesModelComponent {
  val pricesModel: PricesModel

  type PricesModelKey

  trait PricesModel {
    def findByCardId(card_id: String): PricesHistory
    def appendToCard(card_id: String, datetime: DateTime, price: Double)
  }
}

trait PricesModelComponentImpl extends PricesModelComponent {
  this: DBProviderComponent =>

  type PricesModelKey = Pk[Long]

  class PricesModelImpl extends PricesModel {
    /**
     * Parse a single price from a ResultSet
     */
    private val simple =
      get[Long]("prices.dt") ~
      get[Double]("prices.price") map {
        case tstamp~price => new DateTime(tstamp) -> price
      }

    def findByCardId(card_id: String): PricesHistory = {
      DB.withConnection { implicit connection =>
        SQL("SELECT dt, price FROM prices WHERE card_id = {card_id} ORDER BY dt ASC")
          .on("card_id" -> card_id).as(simple *)
      }
    }

    def appendToCard(card_id: String, datetime: DateTime, price: Double) {
      DB.withConnection { implicit connection =>
        SQL("INSERT INTO prices(card_id, dt, price) VALUES ({card_id}, {dt}, {price})")
          .on("card_id" -> card_id, "dt" -> datetime.millis, "price" -> price).executeInsert()
      }
    }
  }
}