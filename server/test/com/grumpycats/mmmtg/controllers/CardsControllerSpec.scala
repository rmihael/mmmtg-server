/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/11/13
 * Time: 12:11 AM
 */

import com.grumpycats.mmmtg.matchers.StarcityMatcherComponentImpl
import com.grumpycats.mmmtg.matchers.stubs.TestGoogleCardSearchComponentImpl
import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._

import com.grumpycats.mmmtg.controllers.CardsServiceComponentImpl
import com.grumpycats.mmmtg.models.stubs._

class CardsControllerSpec extends CardsServiceComponentImpl with TestCardModelComponentImpl
                             with TestPricesModelComponentImpl with TestPriceSourceModelComponentImpl
                             with StarcityMatcherComponentImpl with TestGoogleCardSearchComponentImpl with Specification {
  val cardModel = new CardModelImpl
  val pricesModel = new PricesModelImpl
  val cardsService = new CardsServiceImpl
  val priceSourceModel = new PriceSourceModelImpl
  val cardsMatcher = new CardsMatcherImpl
  val cardSearchApi = new CardSearchApiImpl

  "The Cards controller" should {
    "answer to index call" in {
      running(FakeApplication(additionalConfiguration=inMemoryDatabase())) {
        val result = cardsService.index(FakeRequest())
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
        charset(result) must beSome("utf-8")
      }
    }

    "list cards in response to index call" in {
      running(FakeApplication(additionalConfiguration=inMemoryDatabase())) {
        val result = cardsService.index(FakeRequest())
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
      running(FakeApplication(additionalConfiguration=inMemoryDatabase())) {
        val result = cardsService.findByName("Force of Will", "Alliances")(FakeRequest())
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
        charset(result) must beSome("utf-8")
        val resultJson = Json.parse(contentAsString(result))
        resultJson match {
          case JsObject(Seq(("id", JsString(_)), ("name", JsString(name)), ("block", JsString(_)), ("prices", JsObject(_)))) => name must equalTo("Force of Will")
          case _ => failure("Wrong JSON structure")
        }
      }
    }

    "give 404 for unknown card name" in {
      running(FakeApplication(additionalConfiguration=inMemoryDatabase())) {
        val result = cardsService.findByName("No Card", "Alliances")(FakeRequest())
        status(result) must equalTo(NOT_FOUND)
      }
    }

    "give 404 for unknown card id" in {
      running(FakeApplication(additionalConfiguration=inMemoryDatabase())) {
        val result = cardsService.findById("0")(FakeRequest())
        status(result) must equalTo(NOT_FOUND)
      }
    }

    "answer to create call" in {
      running(FakeApplication(additionalConfiguration=inMemoryDatabase())) {
        val body = Json.parse("""{"name": "Force of Will", "block": "Alliances"}""")
        val result = cardsService.create(FakeRequest().withBody(body))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
        charset(result) must beSome("utf-8")
        val resultJson = Json.parse(contentAsString(result))
        resultJson match {
          case JsObject(Seq(("id", JsString(_)), ("name", JsString(name)), ("block", JsString(_)), ("prices", JsObject(_)))) => name must equalTo("Force of Will")
          case _ => failure("Wrong JSON structure")
        }
      }
    }
  }
}