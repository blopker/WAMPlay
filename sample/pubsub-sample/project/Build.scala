import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "pubsub-sample"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    "com.blopker" % "wamplay_2.10" % "0.0.1"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers += Resolver.url("WAMPlay Repo", url("http://blopker.github.com/releases/"))(Resolver.ivyStylePatterns)
  )
}
