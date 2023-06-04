import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.CourierPage;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }
    @After
    public void deleteCourier() {
        String json = "{\"login\": \"Mugiwara\", \"password\": \"482244\"}";
        CourierPage courierPage = new CourierPage();
        courierPage.deleteCourier(json);
    }
    @Step("Отправка запроса")
    public Response sendRequestCreateCourier(String json) {
        CourierPage createCourier = new CourierPage();
        Response response = createCourier.createCourier(json);
        return response;
    }
    @Step("Проверка кода ошибки и сообщения")
    public void checkStatusCodeAndMessage(Response response, int statusCode, String message) {
        response.then()
                .assertThat()
                .statusCode(statusCode)
                .and()
                .body("message", equalTo(message));
    }
    @Test
    @DisplayName("Check create courier with correct status response")
    @Description("Проверка создания курьера с корректным ответом статуса")
    public void checkCreateCourierWithCorrectStatusResponse() {
        Response response = sendRequestCreateCourier(
                "{\"login\": \"Mugiwara\", \"password\": \"482244\", \"firstName\": \"Luffy\"}");
        response
                .then()
                .assertThat()
                .body("ok", equalTo(true));
    }
    @Test
    @DisplayName("Check create two identical couriers")
    @Description("Проверка создания двух идентичных курьеров")
    public void checkCreateTwoIdenticalCouriers() {
        sendRequestCreateCourier(
                "{\"login\": \"Mugiwara\", \"password\": \"482244\", \"firstName\": \"Luffy\"}");
        Response response = sendRequestCreateCourier(
                "{\"login\": \"Mugiwara\", \"password\": \"482244\", \"firstName\": \"Luffy\"}");
        checkStatusCodeAndMessage(response, 409, "Этот логин уже используется. Попробуйте другой.");
    }
    @Test
    @DisplayName("Check create courier without login")
    @Description("Проверка создания курьера без логина")
    public void checkCreateCourierWithoutLogin() {
        Response response = sendRequestCreateCourier("{\"password\": \"482244\", \"firstName\": \"Luffy\"}");
        checkStatusCodeAndMessage(response, 400, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Check create courier without password")
    @Description("Проверка создания курьера без пароля")
    public void checkCreateCourierWithoutPass() {
        Response response = sendRequestCreateCourier("{\"login\": \"Mugiwara\", \"firstName\": \"Luffy\"}");
        checkStatusCodeAndMessage(response, 400, "Недостаточно данных для создания учетной записи");
    }
    @Test
    @DisplayName("Check create courier with correct status code")
    @Description("Проверка статус-кода при корректном создании пользователя")
    public void checkCreateCourierWithCorrectStatusCode() {
        Response response = sendRequestCreateCourier(
                "{\"login\": \"Mugiwara\", \"password\": \"482244\", \"firstName\": \"Luffy\"}");
        response
                .then()
                .assertThat()
                .statusCode(201);
    }
    @Test
    @DisplayName("Check create couriers with identical login")
    @Description("Проверка создания двух курьеров с одинаковым логином")
    public void checkCreateCouriersWithIdenticalLogin() {
        sendRequestCreateCourier("{\"login\": \"Mugiwara\", \"password\": \"482244\", \"firstName\": \"Monkey D.\"}");
        Response response = sendRequestCreateCourier("{\"login\": \"Mugiwara\", \"password\": \"989644165\"}");
        checkStatusCodeAndMessage(response, 409, "Этот логин уже используется. Попробуйте другой.");
    }
}
