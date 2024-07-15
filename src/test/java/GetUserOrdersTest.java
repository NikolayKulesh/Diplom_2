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

public class GetUserOrdersTest {
    private UserClient userClient;
    private OrderClient orderClient;
    StringBuilder token;

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }

    @Test // проверил получение списка заказов авторизованным пользователем
    @DisplayName("Check the receipt of the list of orders by an authorized user")
    @Description("Check the creation of the user, receipt of the list of user orders, response code, response body")
    public void getOrdersAuthorizedUserTest() {
        userClient = new UserClient();
        Response response = userClient.create("guts@yandex.ru", "12345", "Guts");

        token = new StringBuilder(response.then().extract().path("accessToken").toString());
        token.delete(0,7);

        orderClient = new OrderClient();
        Response orderResponse = orderClient.getOrder(String.valueOf(token));

        orderResponse.then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test // проверил получение списка заказов неавторизованным пользователем
    @DisplayName("Check the receipt of the list of orders by an unauthorized user")
    @Description("Check the receipt of the list of orders by an unauthorized user, the response code, the response body")
    public void getOrdersUnauthorizedUserTest() {
        orderClient = new OrderClient();
        Response orderResponse = orderClient.getOrder("");

        orderResponse.then().assertThat().body("message", equalTo("You should be authorised"))
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
