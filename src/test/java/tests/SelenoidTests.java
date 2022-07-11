package tests;

import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class SelenoidTests {


    @Test
    void testSelenoidStatus(){

        given()

                .when()
                .get("https://selenoid.autotests.cloud/status")
                .then()
                .body("total",is(200));
      }
}
