name := "nonograms"

version := "0.1"

scalaVersion := "2.13.6"

val slf4jVersion = "1.7.30"

libraryDependencies ++= Seq(
  //test-only
  "org.scalatest" %% "scalatest" % "3.2.15" % Test,
  //logging
  "org.slf4j"      % "slf4j-api"       % slf4jVersion,
  "ch.qos.logback" % "logback-classic" % "1.4.6"
)