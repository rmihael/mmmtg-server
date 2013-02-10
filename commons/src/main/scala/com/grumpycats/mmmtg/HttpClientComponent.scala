/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 2/10/13
 * Time: 3:30 AM
 */

package com.grumpycats.mmmtg

import concurrent.{Future, ExecutionContext}

case class HttpResponse(status: Int, body: String)

trait HttpClientComponent {
  val httpClient: HttpClient

  trait HttpClient {
    def get(url: String)(implicit ec: ExecutionContext): Future[HttpResponse]
  }
}
