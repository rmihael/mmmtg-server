/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/13/13
 * Time: 8:58 PM
 */

import com.grumpycats.mmmtg.ComponentsRegistry

import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

class CardModelSpec extends Specification {
  val cardModel = ComponentsRegistry.cardModel

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