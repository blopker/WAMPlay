import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "rpc"
  val appVersion      = "1.0-SNAPSHOT"

  val appDependencies = Seq(
    javaCore,
    "ws.wamplay" %% "wamplay" % "0.1.0"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    resolvers ++= Seq("Bo's Repository" at "http://blopker.github.com/maven-repo/")
  )

}
