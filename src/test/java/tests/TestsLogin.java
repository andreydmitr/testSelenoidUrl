package tests;

import com.codeborne.selenide.Configuration;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;

public class TestsLogin {
    public static void sleepMs(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // this part is executed when an exception (in this example InterruptedException) occurs
            // System.exit();
        }
    }

    @Test
    void testUILogin() {
        Configuration.baseUrl = "https://demowebshop.tricentis.com";
        Configuration.headless = false;

        step("Login", () -> {
            open("/login");
        });
        step("Enter login, pass", () -> {
            $("input.email").setValue("qaguru@qa.guru");
            $("input.password").setValue("qaguru@qa.guru1").pressEnter();
        });
        step("Test auth ok", () -> {
            $(".account").shouldHave(text("qaguru@qa.guru"));
        });
        sleepMs(4000);
    }


    @Test
    void testAPILogin() {
        RestAssured.baseURI = "https://demowebshop.tricentis.com";
        String body = "";
        given()
                .contentType()
                .body(body)
                .log().all()
                .when()
                .post("/login")

                .then()
                .log().status()
                .log().body()
                .statusCode(302);

        step("Test auth ok", () -> {
            $(".account").shouldHave(text("qaguru@qa.guru"));
        });
        sleepMs(4000);
    }

}
