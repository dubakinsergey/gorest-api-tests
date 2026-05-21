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

    // Конструктор — настраивается один раз при создании клиента
    public ApiClient() {

        this.spec = RestAssured.given()
                .baseUri(ConfigManager.getConfig().baseUrl())      // из config.properties
                .header("Authorization", "Bearer " + ConfigManager.getConfig().apiToken())
                .contentType(ContentType.JSON)
                .filter(new AllureRestAssured())                   // логи в Allure
                .log().ifValidationFails();                        // лог в консоль при падении
    }

    // CREATE — создаёт пользователя
    public UserResponse createUser(UserRequest request) {

        return spec
                .body(request)                                     // DTO → JSON
                .when()
                .post("/users")
                .then()
                .statusCode(201)                                 // проверка, что создано
                .extract()
                .as(UserResponse.class);                            // JSON → DTO
    }

    // READ — получает пользователя по ID
    public UserResponse getUser(int userId) {

        return spec
                .when()
                .get("/users/" + userId)
                .then()
                .statusCode(200)                                   // проверка, что найден
                .extract()
                .as(UserResponse.class);
    }

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

    // DELETE — удаляет пользователя
    public void deleteUser(int userId) {

        spec.when()
                .delete("/users/" + userId)
                .then()
                .statusCode(204);                                  // No Content — успешно удалён
    }

    public boolean existsUser(int userId) {
        try {
            spec.when()
                    .get("/users/" + userId)
                    .then()
                    .statusCode(200);
            return true;
        } catch (AssertionError e) {
            return false;
        }
    }
}