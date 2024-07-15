package org.example;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class UserClient {

    @Step("User create, POST request to /api/auth/register")
    public Response create(String email, String password, String name) {
        UserCreate createUser = new UserCreate(email, password, name);
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(createUser)
                .when()
                .post("/api/auth/register");
    }

    @Step("User authorization, POST request to /api/auth/login")
    public Response login(String email, String password) {
        UserLogin loginUser = new UserLogin(email, password);
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(loginUser)
                .when()
                .post("/api/auth/login");
    }

    @Step("User edit, PATCH request to /api/auth/user")
    public Response edit(String email, String name, String token) {
        UserEdit editUser = new UserEdit(email, name);
        return given()
                .auth().oauth2(token)
                .header("Content-type", "application/json")
                .and()
                .body(editUser)
                .when()
                .patch("/api/auth/user");
    }

    @Step("User delete, DELETE request to /api/auth/user")
    public void delete(String token) {
        given()
                .auth().oauth2(token)
                .header("Content-type", "application/json")
                .when()
                .delete("/api/auth/user");
    }
}
