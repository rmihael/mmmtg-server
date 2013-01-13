import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "mmmtg-server"
    val appVersion      = "1.0-SNAPSHOT"

    val appDependencies = Seq(
      "org.mockito" % "mockito-all" % "1.9.0"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      // Add your own project settings here      
    )

}
