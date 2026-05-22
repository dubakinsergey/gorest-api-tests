package com.hasl.gorest.api;

import com.hasl.gorest.client.ApiClient;
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
    private boolean isDeletedByTest = false;

    // ==================== POSITIVE TESTS ====================

    @Test
    public void createUserTest() {

        UserRequest request = UserFactory.validUser();
        UserResponse response = client.createUser(request);
        createdUserId = response.getId();

        assertThat(response.getId()).isPositive();
        assertThat(response.getName()).isEqualTo(request.getName());
        assertThat(response.getEmail()).isEqualTo(request.getEmail());
        assertThat(response.getGender()).isEqualTo(request.getGender());
        assertThat(response.getStatus()).isEqualTo(request.getStatus());
    }

    @Test
    public void getUserTest() {

        UserRequest request = UserFactory.validUser();
        UserResponse created = client.createUser(request);
        createdUserId = created.getId();

        UserResponse fetched = client.getUser(created.getId());

        assertThat(fetched.getId()).isEqualTo(created.getId());
        assertThat(fetched.getName()).isEqualTo(created.getName());
        assertThat(fetched.getEmail()).isEqualTo(created.getEmail());
        assertThat(fetched.getGender()).isEqualTo(created.getGender());
        assertThat(fetched.getStatus()).isEqualTo(created.getStatus());
    }

    @Test
    public void updateUserTest() {

        UserRequest request = UserFactory.validUser();
        UserResponse created = client.createUser(request);
        createdUserId = created.getId();

        UserRequest updatedRequest = UserRequest.builder()
                .name("Обновлённое Имя")
                .email("updated." + System.currentTimeMillis() + "@example.com")
                .gender("female")
                .status("inactive")
                .build();

        UserResponse updated = client.updateUser(created.getId(), updatedRequest);

        assertThat(updated.getName()).isEqualTo(updatedRequest.getName());
        assertThat(updated.getEmail()).isEqualTo(updatedRequest.getEmail());
        assertThat(updated.getGender()).isEqualTo(updatedRequest.getGender());
        assertThat(updated.getStatus()).isEqualTo(updatedRequest.getStatus());
        assertThat(updated.getId()).isEqualTo(created.getId());
    }

    @Test
    public void deleteUserTest() {

        UserRequest request = UserFactory.validUser();
        UserResponse created = client.createUser(request);
        createdUserId = created.getId();

        assertThat(client.existsUser(created.getId())).isTrue();

        client.deleteUser(created.getId());
        isDeletedByTest = true;

        assertThat(client.existsUser(created.getId())).isFalse();
    }

    // ==================== NEGATIVE TESTS ====================

    @Test
    public void createUserWithEmptyNameTest() {

        UserRequest request = UserRequest.builder()
                .name("")
                .email("empty.name." + System.currentTimeMillis() + "@example.com")
                .gender("male")
                .status("active")
                .build();

        var response = RestAssured.given()
                .spec(client.getSpec())
                .body(request)
                .when()
                .post("/users")
                .then()
                .extract()
                .response();

        assertThat(response.statusCode()).isEqualTo(422);
    }

    @Test
    public void createUserWithDuplicateEmailTest() {

        UserRequest request = UserFactory.validUser();
        UserResponse created = client.createUser(request);
        createdUserId = created.getId();

        UserRequest duplicateRequest = UserRequest.builder()
                .name("Другой Хасл")
                .email(created.getEmail())
                .gender("female")
                .status("inactive")
                .build();

        var response = RestAssured.given()
                .spec(client.getSpec())
                .body(duplicateRequest)
                .when()
                .post("/users")
                .then()
                .extract()
                .response();

        assertThat(response.statusCode()).isEqualTo(422);
    }

    @Test
    public void getUserWithInvalidIdTest() {

        var response = RestAssured.given()
                .spec(client.getSpec())
                .when()
                .get("/users/999999999")
                .then()
                .extract()
                .response();

        assertThat(response.statusCode()).isEqualTo(404);
    }

    // ==================== CLEANUP ====================

    @AfterMethod
    public void cleanUp() {
        if (createdUserId != 0 && !isDeletedByTest) {
            client.deleteUser(createdUserId);
        }
        createdUserId = 0;
        isDeletedByTest = false;
    }
}