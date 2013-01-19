import sbt._
import Keys._
import PlayProject._
import com.typesafe.startscript.StartScriptPlugin

object BuildSettings {
  import Dependencies._
  import Resolvers._

  val globalSettings = Seq(
    scalacOptions += "-deprecation",
    libraryDependencies ++= Seq(jodaTime),
    resolvers := Seq(typesafeRepo))

  val projectSettings = Defaults.defaultSettings ++ globalSettings
}

object Resolvers {
  val typesafeRepo = "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
}

object Dependencies {
  val jodaTime = "org.scalaj" %% "scalaj-time" % "0.6"
  val akkaHttp = "com.typesafe.akka" % "akka-actor" % "2.0.5"
  val playFramework = "play" %% "play" % play.core.PlayVersion.current
}

object ApplicationBuild extends Build {
  import Dependencies._
  import BuildSettings._

  override def settings = super.settings ++ org.sbtidea.SbtIdeaPlugin.ideaSettings ++ globalSettings

  val appName         = "mmmtg"
  val appVersion      = "1.0-SNAPSHOT"

  val commons = Project("commons", file("commons"),
                        settings = projectSettings ++
                                   Seq(libraryDependencies ++= Seq(playFramework, jodaTime))
  )

  val server = PlayProject("server", appVersion, Seq(jodaTime), path=file("server"), mainLang=SCALA).settings(
    // Add your own project settings here
  ) dependsOn(commons)

  val indexer = Project("indexer", file("indexer"),
                        settings=projectSettings ++
                                 StartScriptPlugin.startScriptForClassesSettings ++
                                 Seq(libraryDependencies ++= Seq(akkaHttp, jodaTime))
  ) dependsOn(commons)

  val main = Project(appName, file("."),
                     settings = projectSettings ++
                                Seq(StartScriptPlugin.stage in Compile := Unit)
  ) aggregate(commons, server, indexer)
}