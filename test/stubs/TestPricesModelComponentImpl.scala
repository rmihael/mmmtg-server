/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/16/13
 * Time: 3:24 AM
 */

package stubs

import org.scala_tools.time.Imports._

import com.grumpycats.mmmtg.models.PricesModelComponent

trait TestPricesModelComponentImpl extends PricesModelComponent {
  type PricesModelKey = Long

  implicit def String2PricesModelKey(value: String): PricesModelKey = Integer.parseInt(value)

  class PricesModelImpl extends PricesModel {
    def findByCardId(card_id: PricesModelKey) = Seq(DateTime.now -> 1.0)
    def appendToCard(card_id: PricesModelKey, datetime: DateTime, price: Double) {}
  }
}
