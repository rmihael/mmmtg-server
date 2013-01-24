/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/16/13
 * Time: 3:23 AM
 */

package com.grumpycats.mmmtg.models.stubs

import com.grumpycats.mmmtg.models.{PriceSourceModelComponent, PricesModelComponent, CardModelComponent}

trait TestCardModelComponentImpl extends CardModelComponent {
  this: PricesModelComponent with PriceSourceModelComponent =>

  type CardModelKey = Int

  implicit def String2CardModelKey(value: String) = Integer.parseInt(value)
  implicit def CardModelKey2String(key: CardModelKey): String = "%s".format(key)

  class CardModelImpl extends CardModel {
    def findById(id: CardModelKey): Option[Card] = id match {
      case 0 => None
      case _ => Some(Card(id, "Test card", "Block", pricesModel.findByCardId("1"), priceSourceModel.findByCardId("1")))
    }
    def findByNameAndBlock(name: String, block: String): Option[Card] = name match {
      case "No Card" => None
      case _ => Some(Card(1, name, block, pricesModel.findByCardId("1"), priceSourceModel.findByCardId("1")))
    }
    def findAll: Seq[Card] = Seq(Card(1, "Test card 1", "Block 1", pricesModel.findByCardId("1"), priceSourceModel.findByCardId("1")),
                                 Card(2, "Test card 2", "Block 2", pricesModel.findByCardId("2"), priceSourceModel.findByCardId("2")))
    def delete(id: CardModelKey) {}
    def create(name: String, block: String): Option[Card] = Some(Card(1, name, block, Seq(), priceSourceModel.findByCardId("1")))
  }
}
