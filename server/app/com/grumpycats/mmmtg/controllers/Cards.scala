/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/10/13
 * Time: 12:46 AM
 */

package com.grumpycats.mmmtg.controllers

import play.api.mvc._
import play.api.libs.json._
import org.scala_tools.time.Imports._

import com.grumpycats.mmmtg.models._

trait CardsServiceComponent extends CardModelComponent {
  val cardsService: CardsService

  trait CardsService extends Controller {
    def index: Action[AnyContent]
    def create: Action[JsValue]
    def findByName(name: String, block: String): Action[AnyContent]
    def findById(id: String): Action[AnyContent]
  }

  implicit object dateTimeWriter extends Writes[PricesHistory] {
    def writes(datetimes: PricesHistory) = JsObject(datetimes map { case (dt, price) => dt.millis.toString -> JsNumber(price) })
  }

  implicit object cardWriter extends Writes[Card] {
    def writes(card: Card): JsValue = {
      Json.toJson(Map("id" -> JsString(card.id), "name" -> Json.toJson(card.name),
        "block" -> Json.toJson(card.block), "prices" -> Json.toJson(card.prices)))
    }
  }
}

trait CardsServiceComponentImpl extends CardsServiceComponent {
  class CardsServiceImpl extends CardsService {
    def index = Action { request => Ok(Json.toJson(Map("cards" -> cardModel.findAll))) }

    def create = Action(parse.json) { request =>
      val maybeCard = for {
        name <- (request.body \ "name").asOpt[String]
        block <- (request.body \ "block").asOpt[String]
        card <- cardModel.create(name, block)
      } yield card
      maybeCard match {
        case Some(card) => Ok(Json.toJson(card))
        case None => BadRequest(Json.toJson(Map("message" -> "some shit happened")))
      }
    }

    def findByName(name: String, block: String) = Action {
      cardModel.findByNameAndBlock(name, block) match {
        case Some(card) => Ok(Json.toJson(card))
        case _ => NotFound
      }
    }

    def findById(id: String) = Action {
      cardModel.findById(id) match {
        case Some(card) => Ok(Json.toJson(card))
        case _ => NotFound
      }
    }
  }
}