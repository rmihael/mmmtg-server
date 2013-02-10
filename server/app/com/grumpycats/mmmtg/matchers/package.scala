/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/31/13
 * Time: 1:36 AM
 */

package com.grumpycats.mmmtg

import concurrent.{Future, ExecutionContext}

import com.grumpycats.mmmtg.models.PriceSourceType._
import com.grumpycats.mmmtg.models.Card

package object matchers {
  trait CardsMatcherComponent {
    val cardsMatcher: CardsMatcher

    trait CardsMatcher {
      def matchCard(card: Card)(implicit ec: ExecutionContext): Future[Option[String]]
      val priceSource: PriceSourceType
    }
  }
}