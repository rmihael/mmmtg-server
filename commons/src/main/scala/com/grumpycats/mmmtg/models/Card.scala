/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/9/13
 * Time: 11:51 PM
 */

package com.grumpycats.mmmtg.models

import scala.language.postfixOps

import anorm._
import anorm.SqlParser._

import com.grumpycats.mmmtg.models.PriceSourceType._

trait CardModelComponent {
  val cardModel: CardModel

  trait CardModel {
    def findById(id: String): Option[Card]
    def findByNameAndBlock(name: String, block: String): Option[Card]
    def findAll: Seq[Card]
    def delete(id: String)
    def create(name: String, block: String): Option[Card]
    def setPriceSource(id: String, sourceType: PriceSourceType, url: String): Option[String]
  }
}

trait CardModelComponentImpl extends CardModelComponent {
  this: PricesModelComponent with PriceSourceModelComponent with DBProviderComponent =>

  class CardModelImpl extends CardModel {
    /**
     * Parse a Card from a ResultSet
     */
    private val simple =
      get[Pk[Long]]("cards.id") ~
      get[String]("cards.name") ~
      get[String]("blocks.name") map {
        case id~name~block => Card(id.toString, name, block, pricesModel.findByCardId(id.toString),
                                   priceSourceModel.findByCardId(id.toString))
      }

    // -- Queries

    /**
     * Retrieve a Card by id.
     */
    def findById(id: String): Option[Card] = {
      DB.withConnection { implicit connection =>
        SQL("SELECT cards.id, cards.name, blocks.name FROM cards JOIN blocks ON cards.block_id = blocks.id WHERE id = {id}").on(
          'id -> id
        ).as(simple.singleOpt)
      }
    }

    def findByNameAndBlock(name: String, block: String): Option[Card] = {
      DB.withConnection { implicit connection =>
        SQL(
          """SELECT cards.id, cards.name, blocks.name FROM cards JOIN blocks ON cards.block_id = blocks.id
             WHERE lower(cards.name) = lower({cardname})
               AND (lower(blocks.name) = lower({blockname}) OR lower(blocks.shortname) = lower({blockname}))""").on(
          'cardname -> name, 'blockname -> block
        ).as(simple.singleOpt)
      }
    }

    /**
     * Retrieve all cards
     */
    def findAll: Seq[Card] = {
      DB.withConnection { implicit connection =>
        SQL("SELECT cards.id, cards.name, blocks.name FROM cards JOIN blocks ON cards.block_id = blocks.id").as(simple *)
      }
    }

    /**
     * Delete a card.
     */
    def delete(id: String) {
      DB.withConnection { implicit connection =>
        SQL("DELETE FROM cards WHERE id = {id}").on(
          'id -> id
        ).executeUpdate()
      }
    }

    /**
     * Create a card.
     */
    def create(name: String, block: String): Option[Card] = {
      DB.withConnection { implicit connection =>
      // Insert the card
        val id: Option[Long] = SQL(
          """insert into cards(name, block_id) values
             ({name}, (SELECT id from blocks WHERE lower(name)=lower({block}) OR lower(shortname)=lower({block})))""")
          .on("name" -> name, "block" -> block).executeInsert()
        id.map {someId => Card(someId.toString, name, block, Seq(), Map())}
      }
    }

    def setPriceSource(id: String, sourceType: PriceSourceType, url: String) = priceSourceModel.setForCard(id, sourceType, url)
  }
}