package com.hasl.gorest.simple_test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleGetTest {

    private static final String TOKEN = "your_token_here";
    private static final String BASE_URL = "https://gorest.co.in/public/v2";

    @Test
    public void getUsersTest() {
        var response = RestAssured.given()
                .baseUri(BASE_URL)
                .header("Authorization", "Bearer " + TOKEN)
                .contentType(ContentType.JSON)
                .when()
                .get("/users")
                .then()
                .extract()
                .response();

        assertThat(response.statusCode()).isEqualTo(200);

        assertThat(response.asString()).isNotEmpty();

        System.out.println("First 200 chars: " + response.asString().substring(0, Math.min(200, response.asString().length())));
    }
}