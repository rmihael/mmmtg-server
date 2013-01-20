/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/19/13
 * Time: 3:27 PM
 */

package com.grumpycats.mmmtg.models

import java.sql.Connection

trait DBProviderComponent {
  val DB: DBProvider

  trait DBProvider {
    def withConnection[A](block: Connection => A): A
  }
}