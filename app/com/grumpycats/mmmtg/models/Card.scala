/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/9/13
 * Time: 11:51 PM
 */

package com.grumpycats.mmmtg.models

import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._
import play.api.libs.json._

trait Card {
  type Key
  val NoId: Key
  val id: Key
  val name: String
  val block: String

  implicit val Key: Writes[Key]
}

trait CardModelComponent {
  val cardModel: CardModel

  trait CardModel {
    def findById(id: Long): Option[Card]
    def findAll: Seq[Card]
    def delete(id: Long)
    def create(name: String, block: String): Option[Card]
  }
}

trait CardModelComponentImpl extends CardModelComponent {
  case class CardImpl(id: Pk[Long], name: String, block: String) extends Card {
    type Key = Pk[Long]

    implicit object Key extends Writes[Key] {
      def writes(key: Key): JsValue = {
        key match {
          case Id(idVal: Long) => JsNumber(idVal)
          case _ => JsNull
        }
      }
    }
    val NoId = NotAssigned
  }

  class CardModelImpl extends CardModel {
    // -- Parsers

    /**
     * Parse a Card from a ResultSet
     */
    private val simple = {
      get[Pk[Long]]("cards.id") ~
      get[String]("cards.name") ~
      get[String]("blocks.name") map {
        case id~name~block => CardImpl(id, name, block)
      }
    }

    // -- Queries

    /**
     * Retrieve a Card by id.
     */
    def findById(id: Long): Option[Card] = {
      DB.withConnection { implicit connection =>
        SQL("SELECT cards.id, cards.name, blocks.name FROM cards JOIN blocks ON cards.block_id = blocks.id WHERE id = {id}").on(
          'id -> id
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
    def delete(id: Long) {
      DB.withConnection { implicit connection =>
        SQL("DELETE FROM cards WHERE id = {id}").on(
          'id -> id
        ).executeUpdate()
      }
    }

    /**
     * Create a Project.
     */
    def create(name: String, block: String): Option[Card] = {
      DB.withConnection { implicit connection =>
      // Insert the card
        val id: Option[Long] = SQL("insert into cards(name, block_id) values ({name}, (SELECT id from blocks WHERE name={block}))")
          .on("name" -> name, "block" -> block).executeInsert()
        id.map {someId => CardImpl(Id(someId), name, block)}
      }
    }
  }
}