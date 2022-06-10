package com.simulation

import io.gatling.core.scenario.Simulation
import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.http.Predef._


class TestAllApIForSpringWebFlux extends Simulation {
  //http config
  val httpConf = http.baseUrl("http://localhost:8080/app/")
     .header("Accept", "application/json")

  def getAll() = {
    repeat(3) {
      exec(http("Get all Products")
        .get("/")
        .check(status.is(200)))
    }
  }

  def get() = {
    repeat(5) {
      exec(http("Get specific product")
        .get("api/1")
        .check(status.in(200 to 210)))
    }
  }

  def post() = {
    repeat(5) {
      exec(http("Get specific product")
        .get("api/1")
        .check(status.in(200 to 210)))
    }
  }
  //Scenario
  val scn = scenario("Scenario to test GetAll,get,create, update, delete endpoints")
        .exec(getAll())
        .pause(5)
        .exec(get())
        .pause(5)
        .exec(getAll())

  setUp(scn.inject(atOnceUsers(1))).protocols(httpConf)
}
