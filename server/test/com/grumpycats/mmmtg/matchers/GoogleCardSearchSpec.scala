/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/31/13
 * Time: 4:10 AM
 */

package com.grumpycats.mmmtg.matchers

import concurrent.{Await, ExecutionContext, Future}
import concurrent.duration.Duration
import org.specs2.matcher.DataTables
import org.specs2.mock._
import org.specs2.mutable.Specification
import play.api.test.FakeApplication
import play.api.test.Helpers._

import com.grumpycats.mmmtg.{HttpResponse, PlayHttpClientComponentImpl}

class GoogleCardSearchSpec extends GoogleCardSearchApiComponentImpl with PlayHttpClientComponentImpl
                           with Specification with Mockito with DataTables {
  val cardSearchApi = new CardSearchApiImpl("fake search id", "fake key")
  val httpClient = mock[HttpClient]

  val goodGoogleResult = """{
 "kind": "customsearch#search",
 "url": {
  "type": "application/json",
  "template": "https://www.googleapis.com/customsearch/v1?q={searchTerms}&num={count?}&start={startIndex?}&lr={language?}&safe={safe?}&cx={cx?}&cref={cref?}&sort={sort?}&filter={filter?}&gl={gl?}&cr={cr?}&googlehost={googleHost?}&c2coff={disableCnTwTranslation?}&hq={hq?}&hl={hl?}&siteSearch={siteSearch?}&siteSearchFilter={siteSearchFilter?}&exactTerms={exactTerms?}&excludeTerms={excludeTerms?}&linkSite={linkSite?}&orTerms={orTerms?}&relatedSite={relatedSite?}&dateRestrict={dateRestrict?}&lowRange={lowRange?}&highRange={highRange?}&searchType={searchType}&fileType={fileType?}&rights={rights?}&imgSize={imgSize?}&imgType={imgType?}&imgColorType={imgColorType?}&imgDominantColor={imgDominantColor?}&alt=json"
 },
 "queries": {
  "request": [
   {
    "title": "Google Custom Search - Force of Will Alliances",
    "totalResults": "5",
    "searchTerms": "Force of Will Alliances",
    "count": 5,
    "startIndex": 1,
    "inputEncoding": "utf8",
    "outputEncoding": "utf8",
    "safe": "off",
    "cx": "013516264341533754196:noqsy9-g8ek"
   }
  ]
 },
 "context": {
  "title": "starcitygames"
 },
 "searchInformation": {
  "searchTime": 0.256861,
  "formattedSearchTime": "0.26",
  "totalResults": "5",
  "formattedTotalResults": "5"
 },
 "items": [
  {
   "kind": "customsearch#result",
   "title": "Force of Will (Alliances)",
   "htmlTitle": "\u003cb\u003eForce of Will\u003c/b\u003e (\u003cb\u003eAlliances\u003c/b\u003e)",
   "link": "http://sales.starcitygames.com/carddisplay.php?product=14451",
   "displayLink": "sales.starcitygames.com",
   "snippet": "Card Type: Instant Casting Cost: 3 U Card Text: You may pay 1 life and remove a   blue card in your hand from the game instead of paying Force of Will's casting ...",
   "htmlSnippet": "Card Type: Instant Casting Cost: 3 U Card Text: You may pay 1 life and remove a \u003cbr\u003e  blue card in your hand from the game instead of paying \u003cb\u003eForce of Will&#39;s\u003c/b\u003e casting \u003cb\u003e...\u003c/b\u003e",
   "cacheId": "e7-kO3Dq5cEJ",
   "formattedUrl": "sales.starcitygames.com/carddisplay.php?product=14451",
   "htmlFormattedUrl": "sales.starcitygames.com/carddisplay.php?product=14451",
   "pagemap": {
    "cse_image": [
     {
      "src": "http://static.starcitygames.com/sales/cardscans/MAGALL/force_of_will.jpg"
     }
    ],
    "cse_thumbnail": [
     {
      "width": "187",
      "height": "256",
      "src": "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTsLe3Tie5_rRbOL3ZF8GLoMwpLgUmGVKSmIr6LGjShSsrSIoaoqxun92WN"
     }
    ],
    "metatags": [
     {
      "y_key": "095558bcde3b1768"
     }
    ]
   }
  },
  {
   "kind": "customsearch#result",
   "title": "Terese Nielsen - Series One - Alliances - Force of Will (Exclusive ...",
   "htmlTitle": "Terese Nielsen - Series One - \u003cb\u003eAlliances\u003c/b\u003e - \u003cb\u003eForce of Will\u003c/b\u003e (Exclusive \u003cb\u003e...\u003c/b\u003e",
   "link": "http://sales.starcitygames.com/carddisplay.php?product=426256",
   "displayLink": "sales.starcitygames.com",
   "snippet": "Terese Nielsen - Series One - Alliances - Force of Will. Exclusive Magic   Lithographs. Description: Magic: The Gathering Artwork Reproduction ...",
   "htmlSnippet": "Terese Nielsen - Series One - \u003cb\u003eAlliances\u003c/b\u003e - \u003cb\u003eForce of Will\u003c/b\u003e. Exclusive Magic \u003cbr\u003e  Lithographs. Description: Magic: The Gathering Artwork Reproduction \u003cb\u003e...\u003c/b\u003e",
   "cacheId": "qsPrkmCfjkEJ",
   "formattedUrl": "sales.starcitygames.com/carddisplay.php?product=426256",
   "htmlFormattedUrl": "sales.starcitygames.com/carddisplay.php?product=426256",
   "pagemap": {
    "cse_image": [
     {
      "src": "http://static.starcitygames.com/sales/cardscans/MAGART/LithographForceofWill.jpg"
     }
    ],
    "cse_thumbnail": [
     {
      "width": "256",
      "height": "180",
      "src": "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRhv7kbiDpoxHtgluAKXjklT69JlGGsOO7R7rjenrxxMmkitInHB7F2zbM"
     }
    ],
    "metatags": [
     {
      "y_key": "095558bcde3b1768"
     }
    ]
   }
  },
  {
   "kind": "customsearch#result",
   "title": "Force of Will (Rarities & Misprints)",
   "htmlTitle": "\u003cb\u003eForce of Will\u003c/b\u003e (Rarities &amp; Misprints)",
   "link": "http://sales.starcitygames.com/carddisplay.php?product=392505",
   "displayLink": "sales.starcitygames.com",
   "snippet": "Force of Will (Alliances - Color Error). Rarities & Misprints. Card Type: Instant   Casting Cost: 3 U Card Text: You may pay 1 life and remove a blue card in your ...",
   "htmlSnippet": "\u003cb\u003eForce of Will\u003c/b\u003e (\u003cb\u003eAlliances\u003c/b\u003e - Color Error). Rarities &amp; Misprints. Card Type: Instant \u003cbr\u003e  Casting Cost: 3 U Card Text: You may pay 1 life and remove a blue card in your \u003cb\u003e...\u003c/b\u003e",
   "cacheId": "ax3B91F-jJ0J",
   "formattedUrl": "sales.starcitygames.com/carddisplay.php?product=392505",
   "htmlFormattedUrl": "sales.starcitygames.com/carddisplay.php?product=392505",
   "pagemap": {
    "cse_image": [
     {
      "src": "http://static.starcitygames.com/sales/cardscans/MAGMSP/ForceofWilla.jpg"
     }
    ],
    "cse_thumbnail": [
     {
      "width": "268",
      "height": "188",
      "src": "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcST58HCHuo46bu5hRcJ7jhtnyvC8EteEk7l5FnM_fbLKaR9DykMtzZj0LSn"
     }
    ],
    "metatags": [
     {
      "y_key": "095558bcde3b1768"
     }
    ]
   }
  },
  {
   "kind": "customsearch#result",
   "title": "Force Of Will (Foreign Magic Singles: Spanish)",
   "htmlTitle": "\u003cb\u003eForce Of Will\u003c/b\u003e (Foreign Magic Singles: Spanish)",
   "link": "http://sales.starcitygames.com/carddisplay.php?product=137848",
   "displayLink": "sales.starcitygames.com",
   "snippet": "Force Of Will (Alliances). Foreign Magic Singles: Spanish. Card Type: Instant   Casting Cost: 3 U Card Text: You may pay 1 life and remove a blue card in your ...",
   "htmlSnippet": "\u003cb\u003eForce Of Will\u003c/b\u003e (\u003cb\u003eAlliances\u003c/b\u003e). Foreign Magic Singles: Spanish. Card Type: Instant \u003cbr\u003e  Casting Cost: 3 U Card Text: You may pay 1 life and remove a blue card in your \u003cb\u003e...\u003c/b\u003e",
   "cacheId": "F0ekzIlDvUMJ",
   "formattedUrl": "sales.starcitygames.com/carddisplay.php?product=137848",
   "htmlFormattedUrl": "sales.starcitygames.com/carddisplay.php?product=137848",
   "pagemap": {
    "metatags": [
     {
      "y_key": "095558bcde3b1768"
     }
    ]
   }
  },
  {
   "kind": "customsearch#result",
   "title": "Commander Deck - Counterpunch (Magic Sealed Product ...",
   "htmlTitle": "Commander Deck - Counterpunch (Magic Sealed Product \u003cb\u003e...\u003c/b\u003e",
   "link": "http://sales.starcitygames.com/carddisplay.php?product=267358",
   "displayLink": "sales.starcitygames.com",
   "snippet": "Use Ghave, Guru of Spores to create a strangling tangle of vines and vegetation   that will crush all who oppose you! The all-consuming power of growth turns ...",
   "htmlSnippet": "Use Ghave, Guru of Spores to create a strangling tangle of vines and vegetation \u003cbr\u003e  that \u003cb\u003ewill\u003c/b\u003e crush all who oppose you! The all-consuming power of growth turns \u003cb\u003e...\u003c/b\u003e",
   "cacheId": "4RPq3JZXge0J",
   "formattedUrl": "sales.starcitygames.com/carddisplay.php?product=267358",
   "htmlFormattedUrl": "sales.starcitygames.com/carddisplay.php?product=267358",
   "pagemap": {
    "cse_image": [
     {
      "src": "http://static.starcitygames.com/sales/cardscans/VS_TMP/MTG_CDR_Counterpunch.jpg"
     }
    ],
    "cse_thumbnail": [
     {
      "width": "225",
      "height": "225",
      "src": "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcTVWHLgqvE-2JXrIbMislmHJDcSa8AHLfugkF8hGaJajzGcGwINfia-fK4"
     }
    ],
    "metatags": [
     {
      "y_key": "095558bcde3b1768"
     }
    ]
   }
  }
 ]
}"""
  val badGoogleResult = """{
 "kind": "customsearch#search",
 "url": {
  "type": "application/json",
  "template": "https://www.googleapis.com/customsearch/v1?q={searchTerms}&num={count?}&start={startIndex?}&lr={language?}&safe={safe?}&cx={cx?}&cref={cref?}&sort={sort?}&filter={filter?}&gl={gl?}&cr={cr?}&googlehost={googleHost?}&c2coff={disableCnTwTranslation?}&hq={hq?}&hl={hl?}&siteSearch={siteSearch?}&siteSearchFilter={siteSearchFilter?}&exactTerms={exactTerms?}&excludeTerms={excludeTerms?}&linkSite={linkSite?}&orTerms={orTerms?}&relatedSite={relatedSite?}&dateRestrict={dateRestrict?}&lowRange={lowRange?}&highRange={highRange?}&searchType={searchType}&fileType={fileType?}&rights={rights?}&imgSize={imgSize?}&imgType={imgType?}&imgColorType={imgColorType?}&imgDominantColor={imgDominantColor?}&alt=json"
 },
 "queries": {
  "request": [
   {
    "title": "Google Custom Search - Force of Will Alliances",
    "totalResults": "5",
    "searchTerms": "Force of Will Alliances",
    "count": 5,
    "startIndex": 1,
    "inputEncoding": "utf8",
    "outputEncoding": "utf8",
    "safe": "off",
    "cx": "013516264341533754196:noqsy9-g8ek"
   }
  ]
 },
 "context": {
  "title": "starcitygames"
 },
 "searchInformation": {
  "searchTime": 0.256861,
  "formattedSearchTime": "0.26",
  "totalResults": "5",
  "formattedTotalResults": "5"
 }
}"""

  "Google card search" should {
    "parse Google Custom Search response" in {
      running(FakeApplication(additionalConfiguration=inMemoryDatabase())) {
        import ExecutionContext.Implicits.global
        httpClient.get(anyString)(any[ExecutionContext]) returns Future(HttpResponse(200, goodGoogleResult))
        val result = Await.result(cardSearchApi.lookup("Force of Will Alliances"), Duration.Inf)
        result must beSome("http://sales.starcitygames.com/carddisplay.php?product=14451")
      }
    }

    "parse malformed Custom Search response" in {
      running(FakeApplication(additionalConfiguration=inMemoryDatabase())) {
        import ExecutionContext.Implicits.global
        httpClient.get(anyString)(any[ExecutionContext]) returns Future(HttpResponse(200, badGoogleResult))
        val result = Await.result(cardSearchApi.lookup("Force of Will Alliances"), Duration.Inf)
        result must beNone
      }
    }
  }
}
