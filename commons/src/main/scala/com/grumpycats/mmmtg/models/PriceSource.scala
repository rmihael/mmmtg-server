/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/22/13
 * Time: 11:22 PM
 */

package com.grumpycats.mmmtg.models

import scala.language.postfixOps

import anorm._
import anorm.SqlParser._

import PriceSourceType._
import org.apache.commons.validator.routines.UrlValidator

trait PriceSourceModelComponent {
  val priceSourceModel: PriceSourceModel

  type PriceSourceModelKey

  trait PriceSourceModel {
    def findByCardId(cardId: String): Map[PriceSourceType, String]
    def setForCard(cardId: String, sourceType: PriceSourceType, url: String): Option[String]
  }
}

trait PriceSourceModelComponentImpl extends PriceSourceModelComponent {
  this: DBProviderComponent =>

  type PriceSourceModelKey = Pk[Long]

  class PriceSourceModelImpl extends PriceSourceModel {
    private val simple =
      get[String]("pricesources.type") ~
      get[String]("pricesources.url") map {
        case sourceType~url => (PriceSourceType.withName(sourceType), url)
      }

    def findByCardId(cardId: String): Map[PriceSourceType, String] = {
      DB.withConnection { implicit connection =>
        SQL("SELECT type, url FROM pricesources WHERE card_id = {card_id}")
          .on("card_id" -> Integer.parseInt(cardId)).as(simple *).toMap
      }
    }

    def setForCard(cardId: String, sourceType: PriceSourceType, url: String): Option[String] = {
      (new UrlValidator(Seq("http", "https").toArray)).isValid(url) match {
        case true => {
          val parsedCardId = Integer.parseInt(cardId)
          val id: Option[Long] = DB.withConnection { implicit connection =>
          SQL("UPDATE pricesources SET url={url} WHERE card_id={card_id} AND type={type}")
            .on("card_id" -> parsedCardId, "type" -> sourceType.toString, "url" -> url).executeUpdate()
          SQL("""INSERT INTO pricesources(card_id, type, url) SELECT {card_id}, {type}, {url}
                 WHERE NOT EXISTS (SELECT 1 FROM pricesources WHERE card_id={card_id} AND type={type})""")
            .on("card_id" -> parsedCardId, "type" -> sourceType.toString, "url" -> url).executeInsert()
          }
          id map {someId => url}
        }
        case otherwise => None
      }
    }
  }
}