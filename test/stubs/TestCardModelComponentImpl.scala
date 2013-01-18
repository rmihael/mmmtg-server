/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/16/13
 * Time: 3:23 AM
 */

package stubs

import play.api.libs.json._

import com.grumpycats.mmmtg.models.{PricesModelComponent, CardModelComponent}

trait TestCardModelComponentImpl extends CardModelComponent {
  this: PricesModelComponent =>

  type CardModelKey = Int

  implicit object Key extends Writes[CardModelKey] {
    def writes(key: CardModelKey) = { JsNumber(key) }
  }

  implicit def String2CardModelKey(value: String) = Integer.parseInt(value)

  class CardModelImpl extends CardModel {
    def findById(id: CardModelKey): Option[Card] = Some(Card(id, "Test card", "Block", pricesModel.findByCardId("1")))
    def findByNameAndBlock(name: String, block: String): Option[Card] = Some(Card(1, name, block, pricesModel.findByCardId("1")))
    def findAll: Seq[Card] = Seq(Card(1, "Test card 1", "Block 1", pricesModel.findByCardId("1")),
                                 Card(2, "Test card 2", "Block 2", pricesModel.findByCardId("2")))
    def delete(id: CardModelKey) {}
    def create(name: String, block: String): Option[Card] = Some(Card(1, name, block, Seq()))
  }
}
