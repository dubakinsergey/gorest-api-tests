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

    private final ApiClient client = new ApiClient();  // 1 клиент на все тесты
    private int createdUserId;  // запоминаем ID для очистки

    // ==================================================
    // CREATE: проверить создание пользователя
    // ==================================================
    @Test
    public void createUserTest() {
        UserRequest request = UserFactory.validUser();          // берём данные из фабрики
        UserResponse response = client.createUser(request);     // отправляем запрос
        createdUserId = response.getId();                       // сохраняем ID

        assertThat(response.getId()).isPositive();              // ID > 0
        assertThat(response.getName()).isEqualTo(request.getName());
        assertThat(response.getEmail()).isEqualTo(request.getEmail());
        assertThat(response.getGender()).isEqualTo(request.getGender());
        assertThat(response.getStatus()).isEqualTo(request.getStatus());
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