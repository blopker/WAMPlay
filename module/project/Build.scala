import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {
  val appName         = "WAMPlay"
  val appVersion      = "0.1.6"

  val appDependencies = Seq(
  	javaCore
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    organization := "ws.wamplay",
    publishTo := Some(Resolver.file("Bo's Repo", Path.userHome / "code" / "blopker.github.com" / "maven-repo" asFile)),
    publishMavenStyle := true,
    // hack to suppress javadoc error, see: https://play.lighthouseapp.com/projects/82401/tickets/898-javadoc-error-invalid-flag-g-when-publishing-new-module-local#ticket-898-7
    publishArtifact in(Compile, packageDoc) := false
  )

}
