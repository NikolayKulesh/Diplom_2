import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.UserClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;

public class UserCreateTest {
    private UserClient userClient;
    StringBuilder token;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Test // проверил, что создается уникальный пользователь
    @DisplayName("Check the creation of a unique user")
    @Description("Check user creation and response code")
    public void addUserTest() {
        userClient = new UserClient();

        Response response = userClient.create("guts@yandex.ru", "12345", "Guts");
        token = new StringBuilder(response.then().extract().path("accessToken").toString());
        token.delete(0,7);

        assertEquals("Неверный статус код", 200, response.statusCode());
    }

    @Test // проверил, что нельзя создать пользователя, который уже зарегестрирован
    @DisplayName("Check the creation of a user who is already registered")
    @Description("Check the creation of the registered user, the response code and the response body")
    public void identicalUserTest() {
        userClient = new UserClient();

        Response response = userClient.create("guts@yandex.ru", "12345", "Guts");

        token = new StringBuilder(response.then().extract().path("accessToken").toString());
        token.delete(0,7);

        Response doubleResponse = userClient.create("guts@yandex.ru", "12345", "Guts");

        doubleResponse.then().assertThat().body("message", equalTo("User already exists"))
                .and()
                .statusCode(403);
    }

    @Test // проверил, что нелья создать пользователя без одного из обязательных полей.
    @DisplayName("Check the creation of a user without one of the required fields")
    @Description("Check the creation of a user without required fields, the response code and the response body")
    public void requiredFieldsTest() {
        userClient = new UserClient();

        Response response = userClient.create("", "12345", "Guts");

        response.then().assertThat().body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);
    }

    @After
    public void tearDown() {
        if(token != null) {
            userClient.delete(String.valueOf(token));
        }
    }
}
