package com.hasl.gorest.api;

import com.hasl.gorest.client.ApiClient;
import com.hasl.gorest.config.ConfigManager;
import com.hasl.gorest.factories.UserFactory;
import com.hasl.gorest.models.UserRequest;
import com.hasl.gorest.models.UserResponse;
import io.restassured.RestAssured;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserApiTest {

    private final ApiClient client = new ApiClient();
    private int createdUserId;

    @Test
    public void createUserTest() {
        UserRequest request = UserFactory.validUser();

        UserResponse response = client.createUser(request);
        createdUserId = response.getId();

        assertThat(response.getId())
                .as("ID должен быть положительным")
                .isPositive();

        assertThat(response.getName())
                .as("Имя должно совпадать с отправленным")
                .isEqualTo(request.getName());

        assertThat(response.getEmail())
                .as("Email должен совпадать с отправленным")
                .isEqualTo(request.getEmail());
    }

    @AfterMethod
    public void cleanUp() {
        if (createdUserId != 0) {
            RestAssured.given()
                    .baseUri(ConfigManager.getConfig().baseUrl())
                    .header("Authorization", "Bearer " + ConfigManager.getConfig().apiToken())
                    .when()
                    .delete("/users/" + createdUserId)
                    .then()
                    .statusCode(204);
        }
    }
}