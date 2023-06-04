import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.OrderPage;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static io.restassured.RestAssured.given;

@RunWith(Parameterized.class)
public class CreateOrderTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }
    @After
    public void cancelOrder() {
        Response response =
                given()
                        .header("Content-type", "application/json").and()
                        .queryParams("track", track)
                        .when()
                        .put("/api/v1/orders/cancel");
        response.then().assertThat().statusCode(200);
    }
    @Step("Отправка запроса")
    public Response sendRequestCreateOrder() {
        Response response =
                given()
                        .header("Content-type", "application/json").and()
                        .body(OrderPage)
                        .when()
                        .post("/api/v1/orders");
        return response;
    }
    @Step("Проверка успешного создания заказа")
    public void checkSuccessfulCreate(Response response) {
        response.then().assertThat().statusCode(201).and()
                .assertThat().body("$", Matchers.hasKey("track"));
    }
    private OrderPage OrderPage;
    private String track;
    public CreateOrderTest(String message, String[] color) {
        this.OrderPage = new OrderPage("Максим", "Чубура", "Кирова, 70",
                "Фрунзенская", "+79384092124", "2023-06-05", 3,
                color, "Я стану автотестером");
    }
    @Parameterized.Parameters(name = "Цвет самоката. Тестовые данные: {0}")
    public static Object[][] getTestData() {
        return new Object[][]{
                {"1 цвет", new String[]{"GREY"}},
                {"2 цвета", new String[]{"BLACK", "GREY"}},
                {"без цвета", new String[]{}}
        };
    }
    @Test
    @DisplayName("Check create order")
    @Description("Проверка создания заказа")
    public void checkCreateOrder() {
        Response response = sendRequestCreateOrder();
        checkSuccessfulCreate(response);
        track = response.jsonPath().getString("track");
    }
}
