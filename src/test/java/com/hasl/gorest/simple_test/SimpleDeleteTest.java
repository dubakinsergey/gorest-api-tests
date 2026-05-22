package com.hasl.gorest.simple_test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleDeleteTest {

    private static final String TOKEN = "your_token_here";
    private static final String BASE_URL = "https://gorest.co.in/public/v2";

    @Test(groups = {"example", "smoke"})
    public void deleteUserTest() {
        String uniqueEmail = "hasl.delete." + System.currentTimeMillis() + "@mail.ru";

        String createBody = """
                {
                    "name": "Хасл На Удаление",
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

        var deleteResponse = RestAssured.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + TOKEN)
                .when()
                .delete("/users/" + userId)
                .then()
                .extract()
                .response();

        assertThat(deleteResponse.statusCode()).isEqualTo(204);

        var getResponse = RestAssured.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + TOKEN)
                .when()
                .get("/users/" + userId)
                .then()
                .extract()
                .response();

        assertThat(getResponse.statusCode()).isEqualTo(404);
    }
}