package com.simulation


import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class TestFetchAPI extends Simulation{
  //http config
  val httpConf = http.baseUrl("localhost:://")
    .header("Accept", value = "application/json")
    .header("content-type", value="application/json")
  //scenario
  val scn = scenario("Get Products Scenario").
    exec(http("get all products request")
      .get("/api/")
      .check(status is 200))

  //setup
  setUp(scn.inject(atOnceUsers(1000))).protocols(httpConf) //increase the load to 1000
}
