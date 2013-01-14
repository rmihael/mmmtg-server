/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/10/13
 * Time: 12:46 AM
 */

package com.grumpycats.mmmtg.controllers

import play.api._
import mvc._
import play.api.libs.json._

import com.grumpycats.mmmtg.models._
import anorm.{Id, Pk}

trait CardsServiceComponent {
  val cardsService: CardsService

  trait CardsService extends Controller {
    def index: Action[AnyContent]
    def create: Action[JsValue]
  }
}

trait CardsServiceComponentImpl extends CardsServiceComponent {
  this: CardModelComponent =>

  class CardsServiceImpl extends CardsService {
    private implicit object cardWriter extends Writes[Card] {
      def writes(card: Card): JsValue = {
        import card.Key // this import brings implicit JSON conversion for card.id
        Json.toJson(Map("id" -> Json.toJson(card.id), "name" -> Json.toJson(card.name)))
      }
    }

    def index = Action { request => Ok(Json.toJson(Map("cards" -> cardModel.findAll))) }

    def create = Action(parse.json) { request =>
      val maybeCard = for {
        name <- (request.body \ "name").asOpt[String]
        card <- cardModel.create(name)
      } yield card
      maybeCard match {
        case Some(card) => Ok(Json.toJson(card))
        case None => BadRequest(Json.toJson(Map("message" -> "some shit happened")))
      }
    }
  }
}