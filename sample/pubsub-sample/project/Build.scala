import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "pubsub-sample"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "wamplay" % "wamplay_2.10" % "1.0-SNAPSHOT"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers += "Local Play Repository" at "file://Users/ninj0x/bin/play/repository/local"
  )
}
