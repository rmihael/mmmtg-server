package com.grumpycats.mmmtg

/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/13/13
 * Time: 6:00 PM
 */

import com.grumpycats.mmmtg.controllers.CardsServiceComponentImpl
import com.grumpycats.mmmtg.models.{CardModelComponentImpl, PricesModelComponentImpl}

object ComponentsRegistry extends
CardModelComponentImpl with
PricesModelComponentImpl with
CardsServiceComponentImpl {

  val cardModel = new CardModelImpl
  val pricesModel = new PricesModelImpl
  val cardsService = new CardsServiceImpl
}