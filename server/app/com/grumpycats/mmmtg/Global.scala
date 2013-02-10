package com.grumpycats.mmmtg

import com.grumpycats.mmmtg.controllers.CardsService
import play.api.GlobalSettings

/**
 * Created with IntelliJ IDEA.
 * User: rmihael
 * Date: 2/10/13
 * Time: 12:43 AM
 */
object Global extends GlobalSettings {
  override def getControllerInstance[A](controllerClass: Class[A]): A = {
    if (controllerClass == classOf[CardsService]) ComponentsRegistry.cardsService.asInstanceOf[A]
    else super.getControllerInstance(controllerClass)
  }
}
