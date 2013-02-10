/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 2/1/13
 * Time: 11:03 PM
 */

package com.grumpycats.mmmtg

import play.api.libs.ws.WS
import concurrent.{ExecutionContext, Future}

trait PlayHttpClientComponentImpl extends HttpClientComponent {
  class HttpClientImpl() extends HttpClient {
    def get(url: String)(implicit ec: ExecutionContext): Future[HttpResponse] = {
      WS.url(url).get() map {response => HttpResponse(response.status, response.body)}
    }
  }
}
