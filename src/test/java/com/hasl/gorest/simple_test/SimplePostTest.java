package com.hasl.gorest.simple_test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SimplePostTest {

    @Test
    public void createUserTest() {

        String requestBody = """
                {
                    "name": "Хасл Временный",
                    "email": "hasl.temp.2026@mail.ru",
                    "gender": "male",
                    "status": "active"
                }
                """;

        var response = RestAssured.given()
                .baseUri("https://gorest.co.in/public/v2")
                .header("Authorization", "Bearer твой_токен_сюда")
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

        assertThat(response.jsonPath().getString("email")).isEqualTo("hasl.temp.2026@mail.ru");
    }
}