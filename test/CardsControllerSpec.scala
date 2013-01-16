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
import com.grumpycats.mmmtg.models.CardModelComponent

trait TestCardModelComponentImpl extends CardModelComponent {
  type Key = Long

  implicit object Key extends Writes[Key] {
    def writes(key: Key) = { JsNumber(key) }
  }

  class CardModelImpl extends CardModel {
    def findById(id: Key): Option[Card] = {
      Some(Card(id, "Test card", "Block"))
    }
    def findAll: Seq[Card] = {
      Seq(Card(1, "Test card 1", "Block 1"), Card(2, "Test card 2", "Block 2"))
    }
    def delete(id: Key) {}
    def create(name: String, block: String): Option[Card] = {
      Some(Card(1, name, block))
    }
  }
}

object TestComponentsRegistry extends
TestCardModelComponentImpl with
CardsServiceComponentImpl {
  val cardModel = new CardModelImpl
  val cardsService = new CardsServiceImpl
}

class CardsControllerSpec extends Specification {
  val cardsService = TestComponentsRegistry.cardsService

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