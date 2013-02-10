/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/31/13
 * Time: 4:27 AM
 */

package com.grumpycats.mmmtg.matchers.stubs

import com.grumpycats.mmmtg.matchers.CardSearchAPIComponent
import play.api.libs.concurrent.Promise
import concurrent.{ExecutionContext, Future}

trait TestGoogleCardSearchComponentImpl extends CardSearchAPIComponent {
  class CardSearchApiImpl extends CardSearchApi {
    def lookup(query: String)(implicit ec: ExecutionContext): Future[Option[String]] = Future(
      query match {
        case "no result" => None
        case _ => Some("http://some.card.url/some/path")
      }
    )
  }
}
