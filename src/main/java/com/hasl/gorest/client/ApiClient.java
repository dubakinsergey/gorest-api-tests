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

    // Constructor: base URL, token, JSON, Allure logging
    public ApiClient() {
        this.spec = RestAssured.given()
                .baseUri(ConfigManager.getConfig().baseUrl())
                .header("Authorization", "Bearer " + ConfigManager.getConfig().apiToken())
                .contentType(ContentType.JSON)
                .filter(new AllureRestAssured())
                .log().ifValidationFails();
    }

    // POST /users → 201
    public UserResponse createUser(UserRequest request) {
        return spec
                .body(request)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .extract()
                .as(UserResponse.class);
    }

    // GET /users/{id} → 200
    public UserResponse getUser(int userId) {
        return spec
                .when()
                .get("/users/" + userId)
                .then()
                .statusCode(200)
                .extract()
                .as(UserResponse.class);
    }

    // PUT /users/{id} → 200
    public UserResponse updateUser(int userId, UserRequest request) {
        return spec
                .body(request)
                .when()
                .put("/users/" + userId)
                .then()
                .statusCode(200)
                .extract()
                .as(UserResponse.class);
    }

    // PATCH /users/{id} → 200
    public UserResponse patchUser(int userId, Object patchBody) {
        return spec
                .body(patchBody)
                .when()
                .patch("/users/" + userId)
                .then()
                .statusCode(200)
                .extract()
                .as(UserResponse.class);
    }

    // DELETE /users/{id} → 204
    public void deleteUser(int userId) {
        spec
                .when()
                .delete("/users/" + userId)
                .then()
                .statusCode(204);
    }

    // GET /users/{id} → true if 200, false if 404
    public boolean existsUser(int userId) {
        try {
            spec
                    .when()
                    .get("/users/" + userId)
                    .then()
                    .statusCode(200);
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }

    // Getter for RequestSpecification (used in negative tests)
    public RequestSpecification getSpec() {
        return spec;
    }
}