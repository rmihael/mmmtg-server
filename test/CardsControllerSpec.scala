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
import com.grumpycats.mmmtg.models.{CardModelComponent, Card}

trait TestCardModelComponentImpl extends CardModelComponent {
  case class TestCard(id: Long, name: String) extends Card {
    type Key = Long
    val NoId = -1L

    implicit object Key extends Writes[Key] {
      def writes(key: Key) = { JsNumber(key) }
    }
  }

  class CardModelImpl extends CardModel {
    def findById(id: Long): Option[Card] = {
      Some(TestCard(id, "Test card"))
    }
    def findAll: Seq[Card] = {
      Seq(TestCard(1, "Test card 1"), TestCard(2, "Test card 2"))
    }
    def delete(id: Long) {}
    def create(name: String): Option[Card] = {
      Some(TestCard(1, name))
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
              JsObject(Seq(("id", JsNumber(_)), ("name", JsString(_)))),
              JsObject(Seq(("id", JsNumber(_)), ("name", JsString(_))))
            ))))
          ) => success
          case _ => failure("Wrong JSON structure: %s".format(contentAsString(result)))
        }
      }
    }

    "answer to create call" in {
      running(FakeApplication(additionalConfiguration=inMemoryDatabase())) {
        val body = Json.parse("""{"name": "Force of Will"}""")
        val result = cardsService.create(FakeRequest().copy(body=body))
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
        charset(result) must beSome("utf-8")
        val resultJson = Json.parse(contentAsString(result))
        resultJson match {
          case JsObject(Seq(("id", JsNumber(_)), ("name", JsString(name)))) => name must equalTo("Force of Will")
          case _ => failure("Wrong JSON structure")
        }
      }
    }
  }
}