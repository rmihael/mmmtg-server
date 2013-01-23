/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/23/13
 * Time: 12:15 AM
 */

package com.grumpycats.mmmtg.models

import org.specs2.mutable._
import play.api.test.FakeApplication
import play.api.test.Helpers._

import com.grumpycats.mmmtg.PlayDBProviderComponent

class PriceSourceModelSpec extends PriceSourceModelComponentImpl
                           with PlayDBProviderComponent with Specification {
  val priceSourceModel = new PriceSourceModelImpl
  val DB = new DBProviderImpl

  "The price source model" should {
    "be persisted" in {
      running(FakeApplication(additionalConfiguration=inMemoryDatabase())) {
        priceSourceModel.setForCard("1", PriceSourceType.StarCity, "http://some.cool.site.com/some/path")
        val results = priceSourceModel.findByCardId("1")
        results must have size 1
      }
    }

    "not allow malformed URLS" in {
      running(FakeApplication(additionalConfiguration=inMemoryDatabase())) {
        priceSourceModel.setForCard("1", PriceSourceType.StarCity, "sample url") must beNone
      }
    }

    "use upsert for setting source" in {
      running(FakeApplication(additionalConfiguration=inMemoryDatabase())) {
        todo
        priceSourceModel.setForCard("1", PriceSourceType.StarCity, "http://some.cool.site.com/some/path")
        priceSourceModel.setForCard("1", PriceSourceType.StarCity, "http://some.other.cool.site.com/some/path")
        val results = priceSourceModel.findByCardId("1")
        results must have size 1
        results(0).url must equalTo("http://some.other.cool.site.com/some/path")
      }
    }
  }
}