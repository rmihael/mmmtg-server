/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/16/13
 * Time: 3:24 AM
 */

package com.grumpycats.mmmtg.models.stubs

import org.scala_tools.time.Imports._

import com.grumpycats.mmmtg.models.PricesModelComponent

trait TestPricesModelComponentImpl extends PricesModelComponent {
  type PricesModelKey = Long

  implicit def String2PricesModelKey(value: String): PricesModelKey = Integer.parseInt(value)

  class PricesModelImpl extends PricesModel {
    def findByCardId(card_id: String) = Seq(new DateTime(2005, 3, 26, 12, 0, 0, 0) -> 1.0)
    def appendToCard(card_id: String, datetime: DateTime, price: Double) {}
  }
}
