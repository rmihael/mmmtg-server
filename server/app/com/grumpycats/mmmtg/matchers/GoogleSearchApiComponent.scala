/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/31/13
 * Time: 1:56 AM
 */

package com.grumpycats.mmmtg.matchers

import com.github.theon.uri.Uri._
import concurrent.{ExecutionContext, Future}
import play.api.libs.json._

import com.grumpycats.mmmtg.HttpClientComponent

trait CardSearchAPIComponent {
  val cardSearchApi: CardSearchApi

  trait CardSearchApi {
    def lookup(query: String)(implicit ec: ExecutionContext): Future[Option[String]]
  }
}

trait GoogleCardSearchApiComponentImpl extends CardSearchAPIComponent {
  this: HttpClientComponent =>

  class CardSearchApiImpl(val searchId: String, val apiKey: String) extends CardSearchApi {
    def lookup(query: String)(implicit ec: ExecutionContext): Future[Option[String]] = {
      val searchUri = "https://www.googleapis.com/customsearch/v1" ? ("key" -> apiKey) & ("cx" -> searchId) & ("q" -> query)
      httpClient.get(searchUri) map { response => extractCardUrl(Json.parse(response.body)) }
    }

    private[this] def extractCardUrl(searchResult: JsValue): Option[String] = {
      (searchResult \ "items") match {
        case JsArray(items) => items.headOption.flatMap {value => (value \ "link").asOpt[String]}
        case _ => None
      }
    }
  }
}