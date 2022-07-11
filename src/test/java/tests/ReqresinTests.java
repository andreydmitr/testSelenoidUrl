package tests;

import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class ReqresinTests {

    @Test
    void testLogin() {

        String body = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"cityslicka\" }";
        given()
                //.auth().basic(\"user1\",\"1234\")
                .log()
                .all()
                .body(body).contentType(ContentType.JSON)
                .when()
                .post("https://reqres.in/api/login")

                .then()
                .statusCode(200)
                .log()
                .all()
                .body("token", Matchers.is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void testLoginMissing() {

        String body = "{ \"email\": \"eve.holt@reqres.in\" }";
        given()
                //.auth().basic(\"user1\",\"1234\")
                .log()
                .all()
                .body(body).contentType(ContentType.JSON)
                .when()
                .post("https://reqres.in/api/login")

                .then()
                .statusCode(400)
                .log()
                .all()
                .body("error", Matchers.is("Missing password"));
    }
}
