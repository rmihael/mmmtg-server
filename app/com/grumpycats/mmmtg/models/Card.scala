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

case class Card(id: Pk[Long], name: String)

trait CardModelComponent {
  val cardModel: CardModel

  trait CardModel {
    def findById(id: Long): Option[Card]
    def findAll: Seq[Card]
    def delete(id: Long)
    def create(name: String): Option[Card]
  }
}

trait CardModelComponentImpl extends CardModelComponent {
  class CardModelImpl extends CardModel {
    // -- Parsers

    /**
     * Parse a Card from a ResultSet
     */
    private val simple = {
      get[Pk[Long]]("cards.id") ~
        get[String]("cards.name") map {
        case id~name => Card(id, name)
      }
    }

    // -- Queries

    /**
     * Retrieve a Card by id.
     */
    def findById(id: Long): Option[Card] = {
      DB.withConnection { implicit connection =>
        SQL("select * from cards where id = {id}").on(
          'id -> id
        ).as(simple.singleOpt)
      }
    }

    /**
     * Retrieve all cards
     */
    def findAll: Seq[Card] = {
      DB.withConnection {implicit connection =>
        SQL("select * from cards").as(simple *)
      }
    }

    /**
     * Delete a project.
     */
    def delete(id: Long) {
      DB.withConnection { implicit connection =>
        SQL("delete from cards where id = {id}").on(
          'id -> id
        ).executeUpdate()
      }
    }

    /**
     * Create a Project.
     */
    def create(name: String): Option[Card] = {
      DB.withConnection { implicit connection =>
      // Insert the card
        val id: Option[Long] = SQL("insert into cards(name) values ({name})").on("name" -> name).executeInsert()
        id.map {someId => Card(Id(someId), name)}
      }
    }
  }
}