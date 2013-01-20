/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/19/13
 * Time: 3:34 PM
 */

package com.grumpycats.mmmtg

import java.sql.Connection
import play.api.Play.current

import com.grumpycats.mmmtg.models.DBProviderComponent

trait PlayDBProviderComponent extends DBProviderComponent {
  class DBProviderImpl extends DBProvider {
    def withConnection[A](block: Connection => A): A = play.api.db.DB.withConnection(block)
  }
}
