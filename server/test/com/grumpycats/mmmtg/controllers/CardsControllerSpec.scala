/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/11/13
 * Time: 12:11 AM
 */

import com.grumpycats.mmmtg.models.PriceSourceType._
import scala.language.reflectiveCalls

import akka.testkit.{TestKit, TestActorRef}
import com.grumpycats.mmmtg.matchers.StarcityMatcherComponentImpl
import com.grumpycats.mmmtg.matchers.stubs.TestGoogleCardSearchComponentImpl
import com.grumpycats.mmmtg.models.{PriceSourceType, Card}
import org.specs2.mock.Mockito
import org.specs2.mutable._

import play.api.test._
import play.api.test.FakeApplication
import play.api.test.Helpers._
import play.api.libs.json._
import play.api.libs.concurrent.Akka
import play.api.Play.current

import com.grumpycats.mmmtg.controllers.CardsServiceComponentImpl
import com.grumpycats.mmmtg.models.stubs._
import scala.Some

class CardsControllerSpec extends Specification with Mockito {
  val app = new CardsServiceComponentImpl with TestCardModelComponentImpl
    with TestPricesModelComponentImpl with TestPriceSourceModelComponentImpl
    with StarcityMatcherComponentImpl with TestGoogleCardSearchComponentImpl {

    val cardModel = new CardModelImpl
    val pricesModel = new PricesModelImpl
    val cardsService = new CardsServiceImpl
    val priceSourceModel = new PriceSourceModelImpl
    val cardsMatcher = new CardsMatcherImpl
    val cardSearchApi = new CardSearchApiImpl
  }

  val cardStub = Card("1", "Test card", "Alliances", Seq(), Map(StarCity -> "http://some.url/some/path"))

  "The Cards controller" should {
    "answer to index call" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val result = app.cardsService.index(FakeRequest())
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
        charset(result) must beSome("utf-8")
      }
    }

    "list cards in response to index call" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val result = app.cardsService.index(FakeRequest())
        Json.parse(contentAsString(result)) match {
          case JsObject(
          Seq(("cards", JsArray(Seq(
          JsObject(Seq(("id", JsString(_)), ("name", JsString(_)), ("block", JsString(_)), ("prices", JsObject(_)))),
          JsObject(Seq(("id", JsString(_)), ("name", JsString(_)), ("block", JsString(_)), ("prices", JsObject(_))))
          ))))
          ) => success
          case _ => failure("Wrong JSON structure: %s".format(contentAsString(result)))
        }
      }
    }

    "find card by block and name" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val result = app.cardsService.findByName("Test card", "Test block")(FakeRequest())
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
        charset(result) must beSome("utf-8")
        val resultJson = Json.parse(contentAsString(result))
        resultJson match {
          case JsObject(Seq(("id", JsString(_)), ("name", JsString(name)), ("block", JsString(_)), ("prices", JsObject(_)))) => name must equalTo("Test card")
          case _ => failure("Wrong JSON structure")
        }
      }
    }

    "give 404 for unknown card name" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val result = app.cardsService.findByName("No Card", "Test bloc")(FakeRequest())
        status(result) must equalTo(NOT_FOUND)
      }
    }

    "give 404 for unknown card id" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val result = app.cardsService.findById("0")(FakeRequest())
        status(result) must equalTo(NOT_FOUND)
      }
    }

    "answer to create call" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val body = Json.parse( """{"name": "Test card", "block": "Test block"}""")
        val result = app.cardsService.create(FakeRequest().withBody(body))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
        charset(result) must beSome("utf-8")
        val resultJson = Json.parse(contentAsString(result))
        resultJson match {
          case JsObject(Seq(("id", JsString(_)), ("name", JsString(name)), ("block", JsString(_)), ("prices", JsObject(_)))) => name must equalTo("Test card")
          case _ => failure("Wrong JSON structure")
        }
      }
    }

    "refuse to create duplicates" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val app = new CardsServiceComponentImpl with TestCardModelComponentImpl
          with TestPricesModelComponentImpl with TestPriceSourceModelComponentImpl
          with StarcityMatcherComponentImpl with TestGoogleCardSearchComponentImpl {
          val cardModel = mock[CardModel]
          val pricesModel = new PricesModelImpl
          val cardsService = new CardsServiceImpl
          val priceSourceModel = new PriceSourceModelImpl
          val cardsMatcher = new CardsMatcherImpl
          val cardSearchApi = new CardSearchApiImpl
        }

        val body = Json.parse("""{"name": "Test card", "block": "Test block"}""")
        app.cardModel.create("Test card", "Test block") returns Some(cardStub) thenReturns None
        app.cardsService.create(FakeRequest().withBody(body))
        val result = app.cardsService.create(FakeRequest().withBody(body))
        status(result) must equalTo(BAD_REQUEST)
        success
      }
    }

    "matcher actor must set prices source after successful matching" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        val app = new CardsServiceComponentImpl with TestCardModelComponentImpl
          with TestPricesModelComponentImpl with TestPriceSourceModelComponentImpl
          with StarcityMatcherComponentImpl with TestGoogleCardSearchComponentImpl {
          val cardModel = mock[CardModel]
          val pricesModel = new PricesModelImpl
          val cardsService = new CardsServiceImpl
          val priceSourceModel = new PriceSourceModelImpl
          val cardsMatcher = new CardsMatcherImpl
          val cardSearchApi = new CardSearchApiImpl

          implicit val actorSystem = Akka.system
          val actorRef = TestActorRef(MatcherActor)
        }
        app.cardModel.setPriceSource(anyString, any, anyString) returns Some("http://some.card.url/some/path")
        app.actorRef receive cardStub
        there was one(app.cardModel).setPriceSource("1", PriceSourceType.StarCity, "http://some.card.url/some/path")
      }
    }
  }
}