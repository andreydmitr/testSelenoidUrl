package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class SelenoidTests {


    @Test
    void testSelenoidStatus() {
        int resp = get("https://selenoid.autotests.cloud/status")
                .then()
                .extract().path("total");

        System.out.println(resp);

        int expectedResp = 20;
        assertEquals(resp, expectedResp);
    }

    @Test
    void testSelenoidStatusLog() {
        given()
                .log()
                .all()

                .when()
                .get("https://selenoid.autotests.cloud/status")

                .then()
                .statusCode(200)
                .log()
                .all()
                .body("total", is(20));
    }

    @Test
    void testSelenoidStatusLog1() {
        given()
                .log().uri()
                .log().body()

                .when()
                .get("https://selenoid.autotests.cloud/status")

                .then()
                .log().status()
                .log().body()
                .body("browsers.chrome", hasKey("100.0"));
    }


    @Test
    void test401() {
        given()
                .log()
                .all()

                .when()
                .get("https://selenoid.autotests.cloud/wd/hub/status")

                .then()
                .statusCode(401)
                .log()
                .all();
                //.body("total", is(20));
    }

    @Test
    void testAuth() {
        given()
                .auth().basic("user1","1234")
                .log()
                .all()

                .when()
                .get("https://selenoid.autotests.cloud/wd/hub/status")

                .then()
                .statusCode(200)
                .log()
                .all();
        //.body("total", is(20));
    }

}
