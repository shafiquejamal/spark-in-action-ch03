name := """spark-template"""

version := "0.0.1"

scalaVersion := "2.11.8"
val sparkVersion = "1.6.1"

// Change this to another test framework if you prefer
libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-streaming" % sparkVersion,
  "org.apache.spark" %% "spark-streaming-twitter" % sparkVersion,
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)



