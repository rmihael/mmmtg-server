/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/13/13
 * Time: 8:58 PM
 */

import org.scala_tools.time.Imports._
import org.specs2.mutable._

import play.api.test.FakeApplication
import play.api.test.Helpers._

import com.grumpycats.mmmtg.models.CardModelComponentImpl
import stubs.TestPricesModelComponentImpl

class CardModelSpec extends CardModelComponentImpl with TestPricesModelComponentImpl with Specification {
  val cardModel = new CardModelImpl
  val pricesModel = new PricesModelImpl

  "The card model" should {
    "be persisted" in {
      running(FakeApplication(additionalConfiguration=inMemoryDatabase())) {
        val currentCount = cardModel.findAll.size
        cardModel.create("Force or Will", "Alliances")
        cardModel.findAll must have size currentCount+1
      }
    }
  }
}