package com.grumpycats.mmmtg

/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/13/13
 * Time: 6:00 PM
 */

import play.api.Play

import com.grumpycats.mmmtg.controllers.CardsServiceComponentImpl
import com.grumpycats.mmmtg.models.{PriceSourceModelComponentImpl, PricesModelComponentImpl, CardModelComponentImpl}
import com.grumpycats.mmmtg.matchers.{GoogleCardSearchApiComponentImpl, StarcityMatcherComponentImpl}

object ComponentsRegistry extends
CardModelComponentImpl with
PricesModelComponentImpl with
PriceSourceModelComponentImpl with
CardsServiceComponentImpl with
StarcityMatcherComponentImpl with
GoogleCardSearchApiComponentImpl with
PlayHttpClientComponentImpl with
PlayDBProviderComponentImpl {

  val cardModel = new CardModelImpl
  val pricesModel = new PricesModelImpl
  val cardsService = new CardsServiceImpl
  val DB = new DBProviderImpl
  val priceSourceModel = new PriceSourceModelImpl
  val cardsMatcher = new CardsMatcherImpl
  val cardSearchApi = new CardSearchApiImpl(Play.current.configuration.getString("googlesearch.searchId").get,
                                            Play.current.configuration.getString("googlesearch.apiKey").get)
  val httpClient = new HttpClientImpl
}