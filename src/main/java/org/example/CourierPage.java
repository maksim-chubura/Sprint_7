package org.example;

import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierPage {
    @Step("Создание курьера")
    public Response createCourier(Courier courier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier");
    }
    @Step("Авторизация курьера")
    public Response loginCourier(LogCourier logCourier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(logCourier)
                .when()
                .post("/api/v1/courier/login");
    }
    @Step("Удаление курьера")
    public void deleteCourier(LogCourier logCourier) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(logCourier)
                .when()
                .post("/api/v1/courier/login");
        int code = response.statusCode();
        if (code == 200) {
            int courierId = response.jsonPath().getInt("id");
            given()
                    .header("Content-type", "application/json")
                    .and()
                    .delete("/api/v1/courier/" + courierId);
        }
    }
}
