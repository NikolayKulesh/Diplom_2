package org.example;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient {
    @Step("Create order, POST request to /api/orders")
    public Response createOrder(String[] ingredients, String token) {
        OrderCreate addOrder = new OrderCreate(ingredients);
        return given()
                .auth().oauth2(token)
                .header("Content-type", "application/json")
                .and()
                .body(addOrder)
                .when()
                .post("/api/orders");
    }

    @Step("Get order, GET request to /api/orders")
    public Response getOrder(String token) {
        return given()
                .auth().oauth2(token)
                .header("Content-type", "application/json")
                .when()
                .get("/api/orders");
    }
}
