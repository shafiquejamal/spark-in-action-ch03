package com.example

import scala.io.Source.fromFile

object Main extends App {
  
  import org.apache.spark.sql.SparkSession
  
  implicit val spark: SparkSession =
    SparkSession
    .builder()
    .appName("Github push counter")
    .master("local[*]")
    .getOrCreate()
  import spark.implicits._
  
  val inputPath = "../data/ch03githubarchive/2015-03-01-0.json"
  val ghLog = spark.read.json(inputPath).cache()
  val pushes = ghLog.filter("type = 'PushEvent'").cache()
  
  pushes.printSchema()
  println("all events:" + ghLog.count)
  println("only pushes:" + pushes.count)
  pushes.show(5)
  
  val grouped = pushes.groupBy("actor.login").count.cache()
  grouped.show(5)
  val ordered = grouped.orderBy(grouped("count").desc)
  ordered.show(5)
  
  val empPath = "../first-edition/ch03/ghEmployees.txt"
  val employees = Set() ++ (
    for {
      line <- fromFile(empPath).getLines
    } yield line.trim
  )
  val bcEmployees = spark.sparkContext.broadcast(employees)
  
  val isEmp = (user: String) => bcEmployees.value contains user
  val isEmployee = spark.udf.register("isEmpUDF", isEmp)
  
  val filtered = ordered.filter(isEmployee($"login"))
  filtered.show()
  
  spark.stop()
  
}
