/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/24/13
 * Time: 2:47 AM
 */

package com.grumpycats.mmmtg.models.stubs

import com.grumpycats.mmmtg.models.{PriceSourceType, PriceSourceModelComponent}
import PriceSourceType._

trait TestPriceSourceModelComponentImpl extends PriceSourceModelComponent {
  type PriceSourceModelKey = Long

  class PriceSourceModelImpl extends PriceSourceModel {
    def findByCardId(cardId: String) = Map(PriceSourceType.StarCity -> "http://some.cool.url/some/path")
    def setForCard(cardId: String, sourceType: PriceSourceType, url: String) = Some(url)
  }
}
