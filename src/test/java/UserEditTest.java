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

public class UserEditTest {
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

    @Test // проверил, что можно изменить логин авторизованным пользователем
    @DisplayName("Check login can be changed by an authorized user")
    @Description("Check create a user and change his login, response code")
    public void editLoginAuthorizedUserTest() {
        Response editResponse = userClient.edit("griffith@yandex.ru", null, String.valueOf(token));

        assertEquals("Неверный статус код", 200, editResponse.statusCode());
    }

    @Test // проверил, что можно изменить имя авторизованным пользователем
    @DisplayName("Check name can be changed by an authorized user")
    @Description("Check create a user and change his name, response code")
    public void editNameAuthorizedUserTest() {
        Response editResponse = userClient.edit(null, "Griffith", String.valueOf(token));

        assertEquals("Неверный статус код", 200, editResponse.statusCode());
    }

    @Test // проверил, что нельзя изменить логин неавторизованного пользователя
    @DisplayName("Check the change of the login of an unauthorized user")
    @Description("Check the login change for an unauthorized user, the response code, and the response body")
    public void editLoginUnauthorizedUserTest() {
        Response editResponse = userClient.edit("griffith@yandex.ru", null, "");

        editResponse.then().assertThat().body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(401);
    }

    @Test // проверил, что нельзя изменить имя неавторизованного пользователя
    @DisplayName("Check the change of the name of an unauthorized user")
    @Description("Check the name change for an unauthorized user, the response code, and the response body")
    public void editNameUnauthorizedUserTest() {
        Response editResponse = userClient.edit(null, "Griffith", "");

        editResponse.then().assertThat().body("message", equalTo("You should be authorised"))
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
