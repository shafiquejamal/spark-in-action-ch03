package com.example

import org.apache.hadoop.yarn.util.RackResolver
import org.apache.log4j.{Level, Logger}

object Main extends App {
  
  import org.apache.spark.sql.SparkSession
  
  implicit val spark: SparkSession =
    SparkSession
    .builder()
    .appName("Time Usage")
    .config("spark.master", "local")
    .getOrCreate()
//  val rootLogger = Logger.getRootLogger
//  rootLogger.setLevel(Level.ERROR)
//  Logger.getLogger("org").setLevel(Level.OFF)
//  Logger.getLogger("akka").setLevel(Level.OFF)
//  Logger.getLogger(classOf[RackResolver]).setLevel(Level.OFF)
  
  spark.stop()
  
}
