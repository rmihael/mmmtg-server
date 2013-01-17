/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/16/13
 * Time: 3:23 AM
 */

package stubs

import play.api.libs.json._

import com.grumpycats.mmmtg.models.CardModelComponent

trait TestCardModelComponentImpl extends CardModelComponent {
  type CardModelKey = Long

  implicit object Key extends Writes[CardModelKey] {
    def writes(key: CardModelKey) = { JsNumber(key) }
  }

  implicit def String2CardModelKey(value: String) = Integer.parseInt(value)

  class CardModelImpl extends CardModel {
    def findById(id: CardModelKey): Option[Card] = {
      Some(Card(id, "Test card", "Block", Seq()))
    }
    def findAll: Seq[Card] = {
      Seq(Card(1, "Test card 1", "Block 1", Seq()), Card(2, "Test card 2", "Block 2", Seq()))
    }
    def delete(id: CardModelKey) {}
    def create(name: String, block: String): Option[Card] = {
      Some(Card(1, name, block, Seq()))
    }
  }
}
