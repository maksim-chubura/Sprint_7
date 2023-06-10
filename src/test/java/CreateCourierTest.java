import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.Courier;
import org.example.CourierPage;
import org.example.LogCourier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class CreateCourierTest {
    CourierPage courierPage;
    Courier courier;
    LogCourier logCourier;
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
        courier = new Courier("Mugiwara", "482244", "Luffy");
        courierPage = new CourierPage();
    }
    @After
    public void deleteCourier() {
        logCourier = new LogCourier("Mugiwara", "482244");
        CourierPage courierPage = new CourierPage();
        courierPage.deleteCourier(logCourier);
    }
    @Step("Отправка запроса")
    public Response sendRequestCreateCourier(Courier courier) {
        CourierPage createCourier = new CourierPage();
        Response response = createCourier.createCourier(courier);
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
        Response response = sendRequestCreateCourier(courier);
        response
                .then()
                .assertThat()
                .body("ok", equalTo(true));
    }
    @Test
    @DisplayName("Check create two identical couriers")
    @Description("Проверка создания двух идентичных курьеров")
    public void checkCreateTwoIdenticalCouriers() {
        sendRequestCreateCourier(courier);
        Response response = courierPage.createCourier(courier);
        checkStatusCodeAndMessage(response, 409, "Этот логин уже используется. Попробуйте другой.");
    }
    @Test
    @DisplayName("Check create courier without login")
    @Description("Проверка создания курьера без логина")
    public void checkCreateCourierWithoutLogin() {
        courier.setLogin("");
        Response response = sendRequestCreateCourier(courier);
        checkStatusCodeAndMessage(response, 400, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Check create courier without password")
    @Description("Проверка создания курьера без пароля")
    public void checkCreateCourierWithoutPass() {
        courier.setPassword("");
        Response response = sendRequestCreateCourier(courier);
        checkStatusCodeAndMessage(response, 400, "Недостаточно данных для создания учетной записи");
    }
    @Test
    @DisplayName("Check create courier with correct status code")
    @Description("Проверка статус-кода при корректном создании пользователя")
    public void checkCreateCourierWithCorrectStatusCode() {
        Response response = sendRequestCreateCourier(courier);
        response
                .then()
                .assertThat()
                .statusCode(201);
    }
    @Test
    @DisplayName("Check create couriers with identical login")
    @Description("Проверка создания двух курьеров с одинаковым логином")
    public void checkCreateCouriersWithIdenticalLogin() {
        sendRequestCreateCourier(courier);
        courier.setPassword("989644165");
        Response response = sendRequestCreateCourier(courier);
        checkStatusCodeAndMessage(response, 409, "Этот логин уже используется. Попробуйте другой.");
    }
}
