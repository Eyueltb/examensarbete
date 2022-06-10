package com.simulation

import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class TestUpdateDeleteAPI extends  Simulation{
   val httpConf = http.baseUrl("http://localhost:8082/app/")
    .header("Accept", "application/json")
  //scenario
    val scn = scenario("Update Product Scenario")
      //Update Product
    .exec(http("Update specific user")
      .get("/api/product/id")
      .body(RawFileBody("./sc/resource/UpdateProduct.json"))
      .check(status.in(expected =  200 to 210)))

      .pause(3)

      .exec(http("Delete specific user")
        .delete("api/product/2")
        .check(status.in(expected =  89 to 284)))

  //setup
  setUp(scn.inject(atOnceUsers(1000))).protocols(httpConf) //increase the load to 1000

  def getSpecificProduct() = {
    repeat(5) {
      exec(http("Get specific product")
        .get("api/product/1")
        .check(status.in(200 to 210)))
    }
  }

  def getAllProducts() = {
    repeat(3) {
      exec(http("Get all products")
        .get("")
        .check(status.is(200)))
    }
  }
}


