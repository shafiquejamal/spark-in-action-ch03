package com.example

object Main extends App {
  
  import org.apache.spark.sql.SparkSession
  
  implicit val spark: SparkSession =
    SparkSession
    .builder()
    .appName("Time Usage")
    .config("spark.master", "local")
    .getOrCreate()
  
  spark.stop()
  
}
