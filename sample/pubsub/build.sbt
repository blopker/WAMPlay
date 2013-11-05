import play.Project._

name := "pubsub"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
	javaCore,
	"ws.wamplay" %% "wamplay" % "0.2.6-SNAPSHOT"
)

playJavaSettings

resolvers += "Local Play Repository" at "file://D:\\Dev\\Play\\play-2.2.1\\repository\\local"
