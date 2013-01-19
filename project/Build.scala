import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {
  override def settings = super.settings ++ org.sbtidea.SbtIdeaPlugin.ideaSettings

  val appName         = "mmmtg-server"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    "play" %% "play" % play.core.PlayVersion.current,
    "org.scalaj" %% "scalaj-time" % "0.6"
  )

  val commons = Project("commons", file("commons")).settings(
    libraryDependencies ++= appDependencies
  )

  val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
    // Add your own project settings here
  ).dependsOn(commons).aggregate(commons)
}
