/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/11/13
 * Time: 12:11 AM
 */

import anorm.Id
import com.grumpycats.mmmtg.controllers.CardsServiceComponentImpl
import com.grumpycats.mmmtg.models.{CardModelComponent, Card}
import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._

trait TestCardModelComponentImpl extends CardModelComponent {
  class CardModelImpl extends CardModel {
    def findById(id: Long): Option[Card] = {
      Some(Card(Id(id), "Test card"))
    }
    def findAll: Seq[Card] = {
      Seq(Card(Id(1), "Test card 1"), Card(Id(2), "Test card 2"))
    }
    def delete(id: Long) {}
    def create(name: String): Option[Card] = {
      Some(Card(Id(1), name))
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
  "The Cards controller" should {
    "answer to index call" in {
      running(FakeApplication(additionalConfiguration=inMemoryDatabase())) {
        val result = TestComponentsRegistry.cardsService.index(FakeRequest())
        status(result) must equalTo(OK)
        contentType(result) must beSome("application/json")
        charset(result) must beSome("utf-8")
      }
    }

    "list cards in response to index call" in {
      running(FakeApplication(additionalConfiguration=inMemoryDatabase())) {
        val result = TestComponentsRegistry.cardsService.index(FakeRequest())
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
        val result = TestComponentsRegistry.cardsService.create(FakeRequest().copy(body=body))
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