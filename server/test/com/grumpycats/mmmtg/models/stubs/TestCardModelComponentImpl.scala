/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/16/13
 * Time: 3:23 AM
 */

package com.grumpycats.mmmtg.models.stubs

import com.grumpycats.mmmtg.models.CardModelComponent
import com.grumpycats.mmmtg.models.PriceSourceType._
import com.grumpycats.mmmtg.models.Card

trait TestCardModelComponentImpl extends CardModelComponent {
  class CardModelImpl extends CardModel {
    val priceSourceStub = Map(StarCity -> "http://some.url/some/path")
    val pricesStub = Seq()

    def findById(id: String): Option[Card] = id match {
      case "0" => None
      case _ => Some(Card(id, "Test card", "Block", pricesStub, priceSourceStub))
    }
    def findByNameAndBlock(name: String, block: String): Option[Card] = name match {
      case "No Card" => None
      case _ => Some(Card("1", name, block, pricesStub, priceSourceStub))
    }
    def findAll: Seq[Card] = Seq(Card("1", "Test card 1", "Block 1", pricesStub, priceSourceStub),
                                 Card("2", "Test card 2", "Block 2", pricesStub, priceSourceStub))
    def delete(id: String) {}
    def create(name: String, block: String): Option[Card] = Some(Card("1", name, block, Seq(), priceSourceStub))
    def setPriceSource(id: String, sourceType: PriceSourceType, url: String) = Some(url)
  }
}
