package tests;

import io.restassured.http.ContentType;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

import org.json.*;

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
                .body("token", is("QpwL5tke4Pnpja7X4"));
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
                .body("error", is("Missing password"));
    }


    @Test
    void testGet() {
        String resp =
                given()
                        .when()
                        .log().uri()
                        .log().body()
                        .get("https://reqres.in/api/users/23")

                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(404)
                        .extract().asString();
        System.out.println(resp);
        assertEquals(resp, "{}");
    }

    @Test
    void testGet1() {
        String resp =
                given()
                        .when()
                        .log().uri()
                        .log().body()
                        .get("https://reqres.in/api/unknown")

                        .then()
                        .log().status()
                        .log().body()
                        .statusCode(200)
                        //.body("total_pages", is(2));

                        .extract().asString();


        System.out.println(resp);

        JSONObject jo = new JSONObject(resp);
        JSONArray arr = jo.getJSONArray("data");
        for (int i = 0; i < arr.length(); i++) {


            if (arr.getJSONObject(i).getInt("id") == 5) {
                String name = arr.getJSONObject(i).getString("name");
                System.out.println("Name for id:" + name + ".");
                if (!name.equalsIgnoreCase("tigerlily")) {
                    fail();
                }
            }
        }


    }


    @Test
    void testPost() {
        String body = "{ \"name\": \"morpheus\", \"job\": \"leader\" }";
        String resp =
                given()
                        .body(body).contentType(ContentType.JSON)
                        .when()
                        .post("https://reqres.in/api/users")

                        .then()
                        .log().body()
                        .statusCode(201)
                        .extract().asString();
        System.out.println(resp);
        //String expected="{ \"name\": \"morpheus\", \"job\": \"leader\", \"id\": \"227\", \"createdAt\": \"2022-07-11T18:20:07.627Z\" }";
        //expected=expected.replace(" ","");
        //System.out.println(expected);
        assertTrue(resp.contains("\"job\":\"leader\""));
    }

}
