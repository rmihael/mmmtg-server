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
import play.api.libs.concurrent.Akka
import play.api.Play.current

import com.grumpycats.mmmtg.models._
import com.grumpycats.mmmtg.matchers.CardsMatcherComponent
import akka.actor.{Props, Actor}
import concurrent.ExecutionContext
import scala.util.{Success, Failure}

trait CardsService extends Controller {
  def index: Action[AnyContent]
  def create: Action[JsValue]
  def findByName(name: String, block: String): Action[AnyContent]
  def findById(id: String): Action[AnyContent]
}

trait CardsServiceComponent {
  val cardsService: CardsService

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
  this: CardModelComponent with CardsMatcherComponent =>

  class CardsServiceImpl extends CardsService {
    def index = Action { request => Ok(Json.toJson(Map("cards" -> cardModel.findAll))) }

    def create = Action(parse.json) { request =>
      val maybeCard = for {
        name <- (request.body \ "name").asOpt[String]
        block <- (request.body \ "block").asOpt[String]
        card <- cardModel.create(name, block)
      } yield card
      maybeCard map { card =>
        Akka.system.actorOf(Props(new MatcherActor)) ! card
        Ok(Json.toJson(card))
      } getOrElse(BadRequest(Json.toJson(Map("message" -> "some shit happened"))))
    }

    def findByName(name: String, block: String) = Action {
      cardModel.findByNameAndBlock(name, block) map {card => Ok(Json.toJson(card))} getOrElse(NotFound)
    }

    def findById(id: String) = Action {
      cardModel.findById(id) map {card => Ok(Json.toJson(card))} getOrElse(NotFound)
    }
  }

  class MatcherActor extends Actor {
    implicit val executionContext: ExecutionContext = Akka.system.dispatcher
    def receive = {
      case card: Card => {
        //hello kote!
        cardsMatcher.matchCard(card) onComplete {
          case Success(Some(url)) => {
            cardModel.setPriceSource(card.id, cardsMatcher.priceSource, url)
          }
          case Success(None) => null
          case Failure(failure) => null
        }
      }
    }
  }
}