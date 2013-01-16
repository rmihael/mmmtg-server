/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/11/13
 * Time: 12:11 AM
 */

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._

import com.grumpycats.mmmtg.controllers.CardsServiceComponentImpl
import stubs.{TestCardModelComponentImpl, TestPricesModelComponentImpl}

class CardsControllerSpec extends CardsServiceComponentImpl with TestCardModelComponentImpl with TestPricesModelComponentImpl with Specification {
  val cardModel = new CardModelImpl
  val pricesModel = new PricesModelImpl
  val cardsService = new CardsServiceImpl

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
              JsObject(Seq(("id", JsNumber(_)), ("name", JsString(_)), ("block", JsString(_)))),
              JsObject(Seq(("id", JsNumber(_)), ("name", JsString(_)), ("block", JsString(_))))
            ))))
          ) => success
          case _ => failure("Wrong JSON structure: %s".format(contentAsString(result)))
        }
      }
    }

    "answer to create call" in {
      running(FakeApplication(additionalConfiguration=inMemoryDatabase())) {
        val body = Json.parse("""{"name": "Force of Will", "block": "Alliances"}""")
        val result = cardsService.create(FakeRequest().copy(body=body))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
        charset(result) must beSome("utf-8")
        val resultJson = Json.parse(contentAsString(result))
        resultJson match {
          case JsObject(Seq(("id", JsNumber(_)), ("name", JsString(name)), ("block", JsString(_)))) => name must equalTo("Force of Will")
          case _ => failure("Wrong JSON structure")
        }
      }
    }
  }
}