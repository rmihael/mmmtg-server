/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/13/13
 * Time: 8:58 PM
 */

import org.specs2.matcher.DataTables
import org.specs2.mutable._

import play.api.test.FakeApplication
import play.api.test.Helpers._

import com.grumpycats.mmmtg.models.CardModelComponentImpl
import com.grumpycats.mmmtg.models.stubs.TestPricesModelComponentImpl

class CardModelSpec extends CardModelComponentImpl with TestPricesModelComponentImpl with Specification with DataTables {
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

    "have no prices after creating" in {
      running(FakeApplication(additionalConfiguration=inMemoryDatabase())) {
        cardModel.create("Force or Will", "Alliances").get.prices must have size 0
      }
    }

    "find card by set and card name" in {
      running(FakeApplication(additionalConfiguration=inMemoryDatabase())) {
        "Card name"    ||    "Block name"    |
        "Test card"    !!    "Alliances"     |
        "test card"    !!    "alliances"     |
        "test card"    !!    "Alliances"     |
        "test card"    !!    "AI"            |
        "test card"    !!    "ai"            |>
        { (name, block) =>
          val card = cardModel.create("Test Card", "Alliances").get
          val result = cardModel.findByNameAndBlock(name, block)
          cardModel.delete(card.id)
          result must beSome.which { foundCard =>
            foundCard match {
              case Card(card.id, card.name, card.block, _) => true
              case _ => false
            }
          }
        }
      }
    }
  }
}