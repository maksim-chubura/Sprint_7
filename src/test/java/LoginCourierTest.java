import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.Courier;
import org.example.CourierPage;
import org.example.LogCourier;
import org.hamcrest.Matchers;
import org.junit.*;

import static org.hamcrest.Matchers.equalTo;

public class LoginCourierTest {
    CourierPage courierPage = new CourierPage();
    Courier courier;
    LogCourier logCourier;
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
        courier = new Courier("Mugiwara", "482244", "Luffy");
        courierPage.createCourier(courier);
        logCourier = new LogCourier("Mugiwara", "482244");
        courierPage.loginCourier(logCourier);
    }
    @After
    public void deleteCourier() {
        logCourier = new LogCourier("Mugiwara", "482244");
        CourierPage courierPage = new CourierPage();
        courierPage.deleteCourier(logCourier);
    }
    @Step("Отправка запроса")
    public Response sendPostRequestLoginCourier(LogCourier logCourier) {
        CourierPage loginCourier = new CourierPage();
        Response response = loginCourier.loginCourier(logCourier);
        return response;
    }
    @Step("Проверка кода ошибки и сообщения")
    public void checkStatusCodeAndBody(Response response, int statusCode, String message) {
        response.then()
                .assertThat()
                .statusCode(statusCode)
                .and()
                .body("message", equalTo(message));
    }
    @Step("Проверка корректного логина в системе")
    public void checkCorrectLogin(Response response, int statusCode) {
        response.then().assertThat().statusCode(statusCode)
                .and()
                .assertThat().body("$", Matchers.hasKey("id"));
    }
    @Test
    @DisplayName("Check login with all required fields")
    @Description("Проверка логина курьера со всеми необходимыми полями")
    public void checkLoginWithAllRequiredFields() {
        courierPage.createCourier(courier);
        Response response = sendPostRequestLoginCourier(logCourier);
        checkCorrectLogin(response, 200);
    }
    @Test
    @DisplayName("Check login courier with wrong login")
    @Description("Проверка попытки залогиниться с неправильным логином")
    public void checkLoginWithWrongLogin() {
        logCourier.setLogin("Mugivara");
        Response response = sendPostRequestLoginCourier(logCourier);
        checkStatusCodeAndBody(response, 404, "Учетная запись не найдена");
    }
    @Test
    @DisplayName("Check login courier with wrong password")
    @Description("Проверка попытки залогиниться с неправильным паролем")
    public void checkLoginWithWrongPass() {
        logCourier.setPassword("12345");
        Response response = sendPostRequestLoginCourier(logCourier);
        checkStatusCodeAndBody(response, 404, "Учетная запись не найдена");
    }
    @Test
    @DisplayName("Check login without login")
    @Description("Проверка попытки залогиниться с пустым логином")
    public void checkLoginWithoutLogin() {
        logCourier.setLogin("");
        Response response = sendPostRequestLoginCourier(logCourier);
        checkStatusCodeAndBody(response, 400, "Недостаточно данных для входа");
    }
    @Test
    @DisplayName("Check login without password")
    @Description("Проверка попытки залогиниться с пустым паролем")
    public void checkLoginWithoutPass() {
        logCourier.setPassword("");
        Response response = sendPostRequestLoginCourier(logCourier);
        checkStatusCodeAndBody(response, 400, "Недостаточно данных для входа");
    }
    @Test
    @DisplayName("Check login with non existent user")
    @Description("Проверка попытки залогиниться несуществующим пользователем")
    public void checkLoginWithNonExistentUser() {
        logCourier = new LogCourier("roronoa_zoro", "zYV38q7Y");
        Response response = sendPostRequestLoginCourier(logCourier);
        checkStatusCodeAndBody(response, 404, "Учетная запись не найдена");
    }
}
