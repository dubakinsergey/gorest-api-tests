package com.hasl.gorest.simple_test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SimplePatchTest {

    private static final String TOKEN = "your_token_here";
    private static final String BASE_URL = "https://gorest.co.in/public/v2";

    @Test(groups = {"example", "smoke"})
    public void partiallyUpdateUserTest() {
        String uniqueEmail = "hasl.patch." + System.currentTimeMillis() + "@mail.ru";

        String createBody = """
                {
                    "name": "Хасл До Патча",
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

        String patchBody = """
                {
                    "name": "Хасл После Патча",
                    "status": "inactive"
                }
                """;

        var patchResponse = RestAssured.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .body(patchBody)
                .when()
                .patch("/users/" + userId)
                .then()
                .extract()
                .response();

        assertThat(patchResponse.statusCode()).isEqualTo(200);
        assertThat(patchResponse.jsonPath().getString("name")).isEqualTo("Хасл После Патча");
        assertThat(patchResponse.jsonPath().getString("status")).isEqualTo("inactive");
        assertThat(patchResponse.jsonPath().getString("email")).isEqualTo(uniqueEmail);

        RestAssured.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + TOKEN)
                .when()
                .delete("/users/" + userId)
                .then()
                .statusCode(204);
    }
}