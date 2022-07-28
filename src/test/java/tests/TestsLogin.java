package tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.restassured.AllureRestAssured;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Cookie;


import java.util.Map;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;
import static helpers.CustomApiListener.withCustomTemplates;
import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class TestsLogin {
    public static void sleepMs(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            // this part is executed when an exception (in this example InterruptedException) occurs
            // System.exit();
        }
    }

    @AfterEach
    void afterEach() {
        WebDriverRunner.getWebDriver().quit();
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

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        Configuration.baseUrl = "http://demowebshop.tricentis.com";
        RestAssured.baseURI = Configuration.baseUrl;
        final StringBuffer cookieValue = new StringBuffer();

        step("Save cookie for login", () -> {
            cookieValue.append(
                    given()
                            .filter(new AllureRestAssured())
                            .contentType("application/x-www-form-urlencoded")
                            .formParam("Email", "qaguru@qa.guru")
                            .formParam("Password", "qaguru@qa.guru1")
                            .formParam("RememberMe", "false")
                            //.body(format("..%s,%s"),login,pass)
                            .log().all()
                            .when()
                            .post("/login")

                            .then()
                            .log().status()
                            .log().body()
                            .statusCode(302)
                            .extract().cookie("NOPCOMMERCE.AUTH"));
        });

        step("Put cookie value", () -> {
            System.out.println(cookieValue.toString());
            open("/Themes/DefaultClean/Content/images/logo.png");
            Cookie ck = new Cookie("NOPCOMMERCE.AUTH", cookieValue.toString());
            WebDriverRunner.getWebDriver().manage().addCookie(ck);
        });
        step("Open site", () -> {
            open("");
        });
        step("Test auth ok", () -> {
            $(".account").shouldHave(text("qaguru@qa.guru"));
        });

    }

    @Test
    void testAPILoginFilter() {

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        Configuration.baseUrl = "http://demowebshop.tricentis.com";
        RestAssured.baseURI = Configuration.baseUrl;
        final StringBuffer cookieValue = new StringBuffer();

        step("Save cookie for login", () -> {
            cookieValue.append(
                    given()
                            .filter(withCustomTemplates())
                            .contentType("application/x-www-form-urlencoded")
                            .formParam("Email", "qaguru@qa.guru")
                            .formParam("Password", "qaguru@qa.guru1")
                            .formParam("RememberMe", "false")
                            //.body(format("..%s,%s"),login,pass)
                            .log().all()
                            .when()
                            .post("/login")

                            .then()
                            .log().status()
                            .log().body()
                            .statusCode(302)
                            .extract().cookie("NOPCOMMERCE.AUTH"));
        });

        step("Put cookie value", () -> {
            System.out.println(cookieValue.toString());
            open("/Themes/DefaultClean/Content/images/logo.png");
            Cookie ck = new Cookie("NOPCOMMERCE.AUTH", cookieValue.toString());
            WebDriverRunner.getWebDriver().manage().addCookie(ck);
        });
        step("Open site", () -> {
            open("");
        });
        step("Test auth ok", () -> {
            $(".account").shouldHave(text("qaguru@qa.guru"));
        });

    }


    @Test
    void testAPILoginAddToCart() {

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        Configuration.baseUrl = "http://demowebshop.tricentis.com";
        RestAssured.baseURI = Configuration.baseUrl;

        final StringBuffer cookieValue = new StringBuffer();

        step("Save cookie for login", () -> {
            cookieValue.append(
                    given()
                            .filter(withCustomTemplates())
                            .contentType("application/x-www-form-urlencoded")
                            .formParam("Email", "qaguru@qa.guru")
                            .formParam("Password", "qaguru@qa.guru1")
                            .formParam("RememberMe", "false")
                            //.body(format("..%s,%s"),login,pass)
                            .log().all()
                            .when()
                            .post("/login")

                            .then()
                            .log().status()
                            .log().body()
                            .statusCode(302)
                            .extract().cookie("NOPCOMMERCE.AUTH"));
        });

        final StringBuffer good = new StringBuffer();
        good.append("141-inch-laptop");
        final StringBuffer goodAddToCart = new StringBuffer();
        goodAddToCart.append("/addproducttocart/details/31/1");
        final StringBuffer goodCount = new StringBuffer();
        goodCount.append("3");

        step("Add to cart:" + good.toString(), () -> {
            given()
                    .when()
                    .log().all()
                    .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                    .formParam("addtocart_31.EnteredQuantity", goodCount.toString())
                    .cookie("NOPCOMMERCE.AUTH", cookieValue.toString())
                    .post(goodAddToCart.toString())
                    .then()
                    .log().status()
                    .log().body()
                    .statusCode(200)
                    .body("success", is(true))
                    .body("updatetopcartsectionhtml", notNullValue());
        });


        step("Check good is added", () -> {
            step("Put cookie value", () -> {
                open("/Themes/DefaultClean/Content/images/logo.png");
                Cookie ck = new Cookie("NOPCOMMERCE.AUTH", cookieValue.toString());
                WebDriverRunner.getWebDriver().manage().addCookie(ck);
            });
            step("Open site", () -> {
                open("/cart");
            });
            step("Test good", () -> {
                //int count=$$(".cart-item-row").size();
                //Assertions.assertTrue(count>0);
                $$(".cart-item-row")
                        .findBy(text("14.1-inch Laptop"))
                        .$("[value=\"" + goodCount + "\"]").shouldBe(visible);
                $$(".cart-item-row")
                        .findBy(text("14.1-inch Laptop"))
                        .$(".qty input")
                        .setValue("0")
                        .pressEnter();
            });


        });
    }

    @Test
    void testAPIGetCartSize() {

        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
        Configuration.baseUrl = "http://demowebshop.tricentis.com";
        RestAssured.baseURI = Configuration.baseUrl;

        
        
        
        
        final StringBuffer cartSize = new StringBuffer();
        final StringBuffer cartSize1 = new StringBuffer();
        final StringBuffer cookie = new StringBuffer();


//            cookie.append(
//                    given()
//                            .filter(withCustomTemplates())
//                            .contentType("application/x-www-form-urlencoded")
//                            .when()
//
//                            .post("/cart")
//                            .then()
//                            //.log().status()
//                            //.log().body()
//                            .statusCode(200)
//                    .extract().cookie("NOPCOMMERCE.AUTH"));
//
//        System.out.println(cookie.toString());

            cartSize.append(
                    given()
                            .filter(withCustomTemplates())
                            .contentType("application/x-www-form-urlencoded")
                            .when()
                            //.cookie("NOPCOMMERCE.AUTH",cookie.toString())
                            .post("/cart")

                            .then()
                            //.log().status()
                            //.log().body()
                            .statusCode(200)
                            .extract().response().body()
                            .htmlPath().getString("**.findAll { it.@class == 'cart-qty' }"));

        System.out.println("!!!");
        System.out.println(cartSize.toString());

        cookie.append(
        given()
                .when()
                //.log().all()
                .cookie("NOPCOMMERCE.AUTH",cookie.toString())
                .contentType("application/x-www-form-urlencoded; charset=UTF-8")
                .formParam("addtocart_31.EnteredQuantity", "2")
                .post("/addproducttocart/details/31/1")
                .then()
                //.log().status()
                .log().body()
                .statusCode(200)
                .body("success", is(true))
                .body("updatetopcartsectionhtml", notNullValue())
                .extract().cookie("Nop.customer"))
                ;

        System.out.println(cookie.toString());

        cartSize1.append(
                given()
                        .filter(withCustomTemplates())
                        .contentType("application/x-www-form-urlencoded")

                        //.log().all()
                        .when()
                        .cookie("Nop.customer",cookie.toString())
                        .post("/cart")

                        .then()
                        //.log().status()
                        //.log().body()
                        .statusCode(200)
                        .extract().response().body()
                        .htmlPath().getString("**.findAll { it.@class == 'cart-qty' }"));

        System.out.println(cartSize1.toString());
    }


}
