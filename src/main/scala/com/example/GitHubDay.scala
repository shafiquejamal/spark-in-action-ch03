package com.example

import scala.io.Source.fromFile

object GitHubDay extends App {
  
  import org.apache.spark.sql.SparkSession
  
  implicit val spark: SparkSession =
    SparkSession
    .builder()
    //.appName("Github push counter")
    //.master("local[*]")
    .getOrCreate()
  import spark.implicits._
  
  val inputPath = args(0) // "../data/ch03githubarchive/*.json"
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
  
  val empPath = args(1) // "../first-edition/ch03/ghEmployees.txt"
  val employees = Set() ++ (
    for {
      line <- fromFile(empPath).getLines
    } yield line.trim
  )
  val bcEmployees = spark.sparkContext.broadcast(employees)
  
  val isEmp = (user: String) => bcEmployees.value contains user
  val isEmployee = spark.udf.register("isEmpUDF", isEmp)
  
  val filtered = ordered.filter(isEmployee($"login"))
  filtered.write.format(args(3)).save(args(2))
  
  spark.stop()
  
}
