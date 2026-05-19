package com.hasl.gorest.client;

import com.hasl.gorest.config.ConfigManager;
import com.hasl.gorest.models.UserRequest;
import com.hasl.gorest.models.UserResponse;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class ApiClient {

    private final RequestSpecification spec;

    public ApiClient() {

        this.spec = RestAssured.given()
                .baseUri(ConfigManager.getConfig().baseUrl())
                .header("Authorization", "Bearer " + ConfigManager.getConfig().apiToken())
                .contentType(ContentType.JSON)
                .filter(new AllureRestAssured())
                .log()
                .ifValidationFails();
    }

    public UserResponse createUser(UserRequest request) {

        return spec.body(request)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .extract()
                .as(UserResponse.class);
    }
}