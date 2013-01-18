/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/15/13
 * Time: 9:28 PM
 */

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._
import org.scala_tools.time.Imports._

import com.grumpycats.mmmtg.models.PricesModelComponentImpl

class PriceModelSpec extends PricesModelComponentImpl with Specification {
  val pricesModel = new PricesModelImpl
  "The prices model" should {
    "be persisted" in {
      running(FakeApplication(additionalConfiguration=inMemoryDatabase())) {
        val currentCount = pricesModel.findByCardId("1").size
        pricesModel.appendToCard("1", DateTime.now, 1.0)
        pricesModel.findByCardId("1") must have size currentCount+1
      }
    }

    "sorted by datetime" in {
      running(FakeApplication(additionalConfiguration=inMemoryDatabase())) {
        val now = DateTime.now
        pricesModel.appendToCard("1", now, 1.0)
        pricesModel.appendToCard("1", now.minusHours(1), 2.0)
        pricesModel.appendToCard("1", now.minusHours(2), 1.0)
        pricesModel.findByCardId("1").unzip._1 must be equalTo(Seq(now.minusHours(2), now.minusHours(1), now))
      }
    }
  }
}