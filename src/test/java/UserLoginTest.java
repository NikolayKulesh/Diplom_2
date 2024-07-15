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

public class UserLoginTest {
    private UserClient userClient;
    StringBuilder token;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";

        userClient = new UserClient();
        Response response = userClient.create("guts@yandex.ru", "12345", "Guts");

        token = new StringBuilder(response.then().extract().path("accessToken").toString());
        token.delete(0,7);
    }

    @Test // проверил, что можно авторизоваться под существующим пользователем
    @DisplayName("Check authorization under an existing user")
    @Description("Check user creation, authorization, response code")
    public void existUserLoginTest() {
        Response loginResponse = userClient.login("guts@yandex.ru", "12345");

        assertEquals("Неверный статус код", 200, loginResponse.statusCode());
    }

    @Test // проверил, что нельзя авторизоваться с неверным логином и паролем
    @DisplayName("Check authorization with an invalid username and password")
    @Description("Check authorization with invalid username and password, response code, response body")
    public void wrongFieldLoginTest() {
        Response loginResponse = userClient.login("griffith@yandex.ru", "01234");

        loginResponse.then().assertThat().body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }

    @After
    public void tearDown() {
        if(token != null) {
            userClient.delete(String.valueOf(token));
        }
    }
}
