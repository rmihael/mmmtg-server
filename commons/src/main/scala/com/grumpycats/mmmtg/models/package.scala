/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 1/18/13
 * Time: 5:57 PM
 */

package com.grumpycats.mmmtg

import org.scala_tools.time.Imports._

package object models {
  type PricesHistory = Seq[(DateTime, Double)]

  object PriceSourceType extends Enumeration {
    type PriceSourceType = Value
    val StarCity = Value
  }
}
