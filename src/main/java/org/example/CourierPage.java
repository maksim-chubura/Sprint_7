package org.example;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierPage {
    public Response createCourier(String json) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier");
    }
    public Response loginCourier(String json) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .post("/api/v1/courier/login");
    }
    public void deleteCourier(String json) {
        Response response = given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
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
