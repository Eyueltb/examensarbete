package computerdatabase;

import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.util.HashMap;
import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.core.CoreDsl.atOnceUsers;
import static io.gatling.javaapi.http.HttpDsl.http;

public class GetProductsSpringBoot extends Simulation {
    {
        HttpProtocolBuilder httpProtocol = http
                .baseUrl("http://localhost:8084")
                .inferHtmlResources(AllowList(), DenyList())
                .acceptHeader("*/*");

        Map<CharSequence, String> headers_0 = new HashMap<>();
        headers_0.put("Sec-Fetch-Dest", "empty");

        ScenarioBuilder scn = scenario("Get ALL Products")
                .exec(
                        http("Get all products")
                                .get("/api")
                );

        setUp(scn.injectOpen(atOnceUsers(200))).protocols(httpProtocol);
    }
}