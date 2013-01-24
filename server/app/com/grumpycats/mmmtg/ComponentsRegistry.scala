package com.grumpycats.mmmtg

/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/13/13
 * Time: 6:00 PM
 */

import com.grumpycats.mmmtg.controllers.CardsServiceComponentImpl
import models.{PriceSourceModelComponentImpl, CardModelComponentImpl, PricesModelComponentImpl}

object ComponentsRegistry extends
CardModelComponentImpl with
PricesModelComponentImpl with
PriceSourceModelComponentImpl with
CardsServiceComponentImpl with
PlayDBProviderComponent {

  val cardModel = new CardModelImpl
  val pricesModel = new PricesModelImpl
  val cardsService = new CardsServiceImpl
  val DB = new DBProviderImpl
  val priceSourceModel = new PriceSourceModelImpl
}