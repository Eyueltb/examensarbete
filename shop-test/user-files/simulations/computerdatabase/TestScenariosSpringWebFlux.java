package computerdatabase;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.HashMap;
import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;

public class TestScenariosSpringWebFlux extends Simulation {
    {
        HttpProtocolBuilder httpProtocol = http
                .baseUrl("http://localhost:8082")
                .inferHtmlResources(AllowList(), DenyList())
                .acceptHeader("*/*")
                .acceptEncodingHeader("gzip, deflate")
                .acceptLanguageHeader("en-US,en;q=0.9,sv;q=0.8")
                .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36");

        Map<CharSequence, String> headers_0 = new HashMap<>();
        headers_0.put("Sec-Fetch-Dest", "empty");
        headers_0.put("Sec-Fetch-Mode", "cors");
        headers_0.put("Sec-Fetch-Site", "same-origin");
        headers_0.put("sec-ch-ua", " Not A;Brand\";v=\"99\", \"Chromium\";v=\"102\", \"Google Chrome\";v=\"102");
        headers_0.put("sec-ch-ua-mobile", "?0");
        headers_0.put("sec-ch-ua-platform", "Windows");

        Map<CharSequence, String> headers_2 = new HashMap<>();
        headers_2.put("Content-Type", "application/json");
        headers_2.put("Origin", "http://localhost:8082");
        headers_2.put("Sec-Fetch-Dest", "empty");
        headers_2.put("Sec-Fetch-Mode", "cors");
        headers_2.put("Sec-Fetch-Site", "same-origin");
        headers_2.put("sec-ch-ua", " Not A;Brand\";v=\"99\", \"Chromium\";v=\"102\", \"Google Chrome\";v=\"102");
        headers_2.put("sec-ch-ua-mobile", "?0");
        headers_2.put("sec-ch-ua-platform", "Windows");

        Map<CharSequence, String> headers_4 = new HashMap<>();
        headers_4.put("Origin", "http://localhost:8082");
        headers_4.put("Sec-Fetch-Dest", "empty");
        headers_4.put("Sec-Fetch-Mode", "cors");
        headers_4.put("Sec-Fetch-Site", "same-origin");
        headers_4.put("sec-ch-ua", " Not A;Brand\";v=\"99\", \"Chromium\";v=\"102\", \"Google Chrome\";v=\"102");
        headers_4.put("sec-ch-ua-mobile", "?0");
        headers_4.put("sec-ch-ua-platform", "Windows");


        ScenarioBuilder scn = scenario("ScenariosSpringWebFlux1")
                .exec(
                        http("Get all")
                                .get("/api")
                                .headers(headers_0)
                )
                .pause(12)
                .exec(
                        http("Get product")
                                .get("/api/50634668-447e-41b4-9db2-3ecba27f0d31")
                                .headers(headers_0)
                )
                .pause(30)
                .exec(
                        http("Create product ")
                                .post("/api")
                                .headers(headers_2)
                                .body(RawFileBody("scenariosspringwebflux/0002_request.json"))
                )
                .pause(32)
                .exec(
                        http("Update product")
                                .put("/api/50634668-447e-41b4-9db2-3ecba27f0d31")
                                .headers(headers_2)
                                .body(RawFileBody("scenariosspringwebflux/0003_request.json"))
                )
                .pause(9)
                .exec(
                        http("Delete Product ")
                                .delete("/api/50634668-447e-41b4-9db2-3ecba27f0d31")
                                .headers(headers_4)
                )
                .pause(9)
                .exec(
                        http("Delete All")
                                .delete("/api")
                                .headers(headers_4)
                );

        setUp(scn.injectOpen(atOnceUsers(50))).protocols(httpProtocol);
    }
}

