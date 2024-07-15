import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.OrderClient;
import org.example.UserClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;

public class OrderCreateTest {
    private UserClient userClient;
    private OrderClient orderClient;
    StringBuilder token;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Test // проверил создание заказа с ингридиентами авторизованным пользователем
    @DisplayName("Check the creation of an order with ingredients for authorized users")
    @Description("Check user creation, order creation with ingredients, response code, response body")
    public void orderCreateAuthorizedUserTest() {
        userClient = new UserClient();
        Response response = userClient.create("guts@yandex.ru", "12345", "Guts");

        token = new StringBuilder(response.then().extract().path("accessToken").toString());
        token.delete(0,7);

        orderClient = new OrderClient();
        Response orderResponse = orderClient.createOrder(new String[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"}, String.valueOf(token));

        orderResponse.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test // проверил создание заказа c ингридиентами неавторизованным пользователем
    @DisplayName("Check the creation of an order with ingredients by an unauthorized user")
    @Description("Check the creation of an order with ingredients by an unauthorized user, the response code, the response body")
    public void orderCreateUnauthorizedUserTest() {
        orderClient = new OrderClient();
        Response orderResponse = orderClient.createOrder(new String[]{"61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa6f"}, "");

        orderResponse.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test // проверил создание заказа без ингредиентов
    @DisplayName("Check the creation of an order without ingredients")
    @Description("Check the creation of an order without ingredients, the response body, the response code")
    public void orderCreateWithoutIngredientsTest() {
        orderClient = new OrderClient();
        Response orderResponse = orderClient.createOrder(new String[]{}, "");

        orderResponse.then().assertThat().body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .statusCode(400);
    }

    @Test // проверил создание заказа c неверным хэшем ингредиентов
    @DisplayName("Check the creation of an order with an incorrect ingredient id")
    @Description("Check the creation of an order with an incorrect ingredient id, response code, response body")
    public void orderCreateWithWrongIngredientsTest() {
        orderClient = new OrderClient();
        Response orderResponse = orderClient.createOrder(new String[]{"122252cv23525", "4536433gh24353"}, "");

        assertEquals("Неверный статус код", 500, orderResponse.statusCode());
    }

    @After
    public void tearDown() {
        if(token != null) {
            userClient.delete(String.valueOf(token));
        }
    }
}
