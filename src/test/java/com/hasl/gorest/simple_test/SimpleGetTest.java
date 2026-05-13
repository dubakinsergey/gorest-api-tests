package com.hasl.gorest.simple_test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleGetTest {

    @Test
    public void getUsersTest() {

        // 1. Отправляем GET запрос
        var response = RestAssured.given()
                .baseUri("https://gorest.co.in/public/v2")
                .header("Authorization", "Bearer твой_токен_сюда")
                .contentType(ContentType.JSON)
                .when()
                .get("/users")
                .then()
                .extract()
                .response();

        // 2. Проверяем статус
        assertThat(response.statusCode()).isEqualTo(200);

        // 3. Проверяем, что тело не пустое
        String body = response.asString();
        assertThat(body).isNotEmpty();

        // 4. Выводим первых двух пользователей (для наглядности)
        System.out.println("Response body (первые 200 символов): " + body.substring(0, Math.min(200, body.length())));
    }
}