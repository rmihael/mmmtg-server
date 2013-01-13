package com.grumpycats.mmmtg

/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/13/13
 * Time: 6:00 PM
 */

import com.grumpycats.mmmtg.controllers.CardsServiceComponentImpl
import com.grumpycats.mmmtg.models.CardModelComponentImpl

object ComponentsRegistry extends
CardModelComponentImpl with
CardsServiceComponentImpl {

  val cardModel = new CardModelImpl
  val cardsService = new CardsServiceImpl
}