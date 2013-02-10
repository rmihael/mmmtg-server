import sbt._
import Keys._
import play.Project._
import com.typesafe.sbt.SbtStartScript

object BuildSettings {
  import Dependencies._
  import Resolvers._

  val globalSettings = Seq(
    scalaVersion := "2.10.0",
    scalacOptions += "-deprecation",
    scalacOptions += "-feature",
    libraryDependencies ++= Seq(),
    resolvers := Seq(typesafeRepo, projectRepo)
  )

  val projectSettings = Defaults.defaultSettings ++ globalSettings
}

object Resolvers {
  val typesafeRepo = "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/"
  val projectRepo = Resolver.file("project-repo", file("localrepo"))(Resolver.ivyStylePatterns)
}

object Dependencies {
  val playFramework = "play" %% "play" % play.core.PlayVersion.current
  val validators = "commons-validator" % "commons-validator" % "1.4.0"
  val mockito = "org.mockito" % "mockito-all" % "1.9.5"
  val jodaTime = "org.scalaj" %% "scalaj-time" % "0.6"
  val uri = "com.github.theon" %% "scala-uri" % "0.3.2"
}

object ApplicationBuild extends Build {
  import Dependencies._
  import BuildSettings._

  override def settings = super.settings ++ globalSettings

  val appName         = "mmmtg"
  val appVersion      = "0.1-SNAPSHOT"

  lazy val commons = Project("commons", file("commons"),
                        settings = projectSettings ++
                                   Seq(libraryDependencies ++= Seq(anorm, jodaTime, validators))
  )

  lazy val server = play.Project("server", appVersion, Seq(jodaTime, mockito, jdbc, uri), path=file("server")).settings(
  ) dependsOn(commons)

  lazy val indexer = Project("indexer", file("indexer"),
                        settings=projectSettings ++
                                 SbtStartScript.startScriptForClassesSettings ++
                                 Seq(libraryDependencies ++= Seq())
  ) dependsOn(commons)

  lazy val main = Project(appName, file("."),
                     settings = projectSettings ++
                                Seq(SbtStartScript.stage in Compile := Unit)
  ) aggregate(commons, server, indexer)
}