package com.hasl.gorest.simple_test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SimplePostTest {

    private static final String TOKEN = "your_token_here";
    private static final String BASE_URL = "https://gorest.co.in/public/v2";

    private int createdUserId;

    @Test(groups = {"example", "smoke"})
    public void createUserTest() {
        String uniqueEmail = "hasl.temp." + System.currentTimeMillis() + "@mail.ru";

        String requestBody = """
                {
                    "name": "Хасл Временный",
                    "email": "%s",
                    "gender": "male",
                    "status": "active"
                }
                """.formatted(uniqueEmail);

        var response = RestAssured.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/users")
                .then()
                .extract()
                .response();

        assertThat(response.statusCode()).isEqualTo(201);
        assertThat(response.jsonPath().getInt("id")).isPositive();
        assertThat(response.jsonPath().getString("name")).isEqualTo("Хасл Временный");
        assertThat(response.jsonPath().getString("email")).isEqualTo(uniqueEmail);

        createdUserId = response.jsonPath().getInt("id");
    }

    @AfterMethod
    public void cleanUp() {
        if (createdUserId != 0) {
            RestAssured.given()
                    .baseUri(BASE_URL)
                    .header("Authorization", "Bearer " + TOKEN)
                    .when()
                    .delete("/users/" + createdUserId)
                    .then()
                    .statusCode(204);
        }
    }
}