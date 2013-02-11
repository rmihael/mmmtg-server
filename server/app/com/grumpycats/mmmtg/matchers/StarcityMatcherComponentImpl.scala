/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/31/13
 * Time: 1:35 AM
 */

package com.grumpycats.mmmtg.matchers

import concurrent.{ExecutionContext, Future}

import com.grumpycats.mmmtg.models.{Card, PriceSourceModelComponent, CardModelComponent, PriceSourceType}
import com.grumpycats.mmmtg.models.PriceSourceType._

trait StarcityMatcherComponentImpl extends CardsMatcherComponent {
  this: CardSearchAPIComponent =>

  class CardsMatcherImpl extends CardsMatcher {
    val priceSource: PriceSourceType = PriceSourceType.StarCity

    def matchCard(card: Card)(implicit ec: ExecutionContext) = cardSearchApi.lookup("%s %s" format (card.name, card.block))
  }
}
