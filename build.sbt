name := "tweeter_streamprocessing"

version := "1.0"

scalaVersion := "3.3.0"

lazy val akkaVersion = "2.8.4"
fork := true

resolvers += "Akka library repository".at("https://repo.akka.io/maven")

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor-typed" % akkaVersion cross CrossVersion.for3Use2_13,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion cross CrossVersion.for3Use2_13,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test cross CrossVersion.for3Use2_13,
  "ch.qos.logback" % "logback-classic" % "1.2.6",
  "org.apache.logging.log4j" % "log4j-slf4j-impl" % "2.14.1"
)
