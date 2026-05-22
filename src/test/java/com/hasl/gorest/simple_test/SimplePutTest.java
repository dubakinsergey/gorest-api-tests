package com.hasl.gorest.simple_test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SimplePutTest {

    private static final String TOKEN = "your_token_here";
    private static final String BASE_URL = "https://gorest.co.in/public/v2";

    @Test(groups = {"example", "smoke"})
    public void updateUserTest() {
        String uniqueEmail = "hasl.put.before." + System.currentTimeMillis() + "@mail.ru";

        String createBody = """
                {
                    "name": "Хасл До Обновления",
                    "email": "%s",
                    "gender": "male",
                    "status": "active"
                }
                """.formatted(uniqueEmail);

        var createResponse = RestAssured.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .body(createBody)
                .when()
                .post("/users")
                .then()
                .extract()
                .response();

        assertThat(createResponse.statusCode()).isEqualTo(201);
        int userId = createResponse.jsonPath().getInt("id");

        String updatedEmail = "hasl.put.after." + System.currentTimeMillis() + "@mail.ru";

        String updateBody = """
                {
                    "name": "Хасл После Обновления",
                    "email": "%s",
                    "gender": "female",
                    "status": "inactive"
                }
                """.formatted(updatedEmail);

        var updateResponse = RestAssured.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .body(updateBody)
                .when()
                .put("/users/" + userId)
                .then()
                .extract()
                .response();

        assertThat(updateResponse.statusCode()).isEqualTo(200);
        assertThat(updateResponse.jsonPath().getString("name")).isEqualTo("Хасл После Обновления");
        assertThat(updateResponse.jsonPath().getString("email")).isEqualTo(updatedEmail);
        assertThat(updateResponse.jsonPath().getString("status")).isEqualTo("inactive");

        RestAssured.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + TOKEN)
                .when()
                .delete("/users/" + userId)
                .then()
                .statusCode(204);
    }
}