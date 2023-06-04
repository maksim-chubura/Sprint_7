import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.CourierPage;
import org.hamcrest.Matchers;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class LoginCourierTest {
    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
        String json = "{\"login\": \"Mugiwara\", \"password\": \"482244\", \"firstName\": \"Luffy\"}";
        CourierPage createCourier = new CourierPage();
        createCourier.createCourier(json);
    }
    @AfterClass
    public static void deleteCourier() {
        String json = "{\"login\": \"Mugiwara\", \"password\": \"482244\"}";
        CourierPage courierPage = new CourierPage();
        courierPage.deleteCourier(json);
    }
    @Step("Отправка запроса")
    public Response sendPostRequestLoginCourier(String json) {
        CourierPage logInCourier = new CourierPage();
        Response response = logInCourier.loginCourier(json);
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
        Response response = sendPostRequestLoginCourier(
                "{\"login\": \"Mugiwara\", \"password\": \"482244\"}");
        checkCorrectLogin(response, 200);
    }
    @Test
    @DisplayName("Check login courier with wrong login")
    @Description("Проверка попытки залогиниться с неправильным логином")
    public void checkLoginWithWrongLogin() {
        Response response = sendPostRequestLoginCourier(
                "{\"login\": \"Mugivara\", \"password\": \"482244\"}");
        checkStatusCodeAndBody(response, 404, "Учетная запись не найдена");
    }
    @Test
    @DisplayName("Check login courier with wrong password")
    @Description("Проверка попытки залогиниться с неправильным паролем")
    public void checkLoginWithWrongPass() {
        Response response = sendPostRequestLoginCourier(
                "{\"login\": \"Mugiwara\", \"password\": \"12345\"}");
        checkStatusCodeAndBody(response, 404, "Учетная запись не найдена");
    }
    @Test
    @DisplayName("Check login without login")
    @Description("Проверка попытки залогиниться с пустым логином")
    public void checkLoginWithoutLogin() {
        Response response = sendPostRequestLoginCourier(
                "{\"login\": \"\", \"password\": \"482244\"}");
        checkStatusCodeAndBody(response, 400, "Недостаточно данных для входа");
    }
    @Test
    @DisplayName("Check login without password")
    @Description("Проверка попытки залогиниться с пустым паролем")
    public void checkLoginWithoutPass() {
        Response response = sendPostRequestLoginCourier(
                "{\"login\": \"Mugiwara\", \"password\": \"\"}");
        checkStatusCodeAndBody(response, 400, "Недостаточно данных для входа");
    }
    @Test
    @DisplayName("Check login with non existent user")
    @Description("Проверка попытки залогиниться несуществующим пользователем")
    public void checkLoginWithNonExistentUser() {
        Response response = sendPostRequestLoginCourier(
                "{\"login\": \"roronoa_zoro\", \"password\": \"zYV38q7Y\"}");
        checkStatusCodeAndBody(response, 404, "Учетная запись не найдена");
    }
}
