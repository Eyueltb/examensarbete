package com.simulation

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._


 class TestPostAPI extends Simulation {
  //http config
  val httpConf = http.baseUrl("localhost:://")
    .header("Accept", value = "application/json")
  //scenario
  val scn = scenario("Add Product Scenario").
    exec(http("add product request")
      .post("/api/user")
      .body(RawFileBody("./src/test/AddProduct.json"))
      .header("content-type", value = "application/json")
      .check(status is 200))

    .pause(3)

    .exec(http("get product request")
      .get("/api/id")
      .check(status is 200))

    .pause(2)

    .exec(http("get all product request")
      .get("/api/")
      .check(status is 200))
  //setup
   setUp(scn.inject(atOnceUsers(1000))).protocols(httpConf) //increase the load to 1000
 }
