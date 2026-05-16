package com.hasl.gorest.models;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserModelTest {

    @Test
    public void userRequestBuilderWorks() {
        UserRequest request = UserRequest.builder()
                .name("Тестовый Хасл")
                .email("test@mail.ru")
                .gender("male")
                .status("active")
                .build();

        assertThat(request.getName())
                .as("Имя должно быть 'Тестовый Хасл'")
                .isEqualTo("Тестовый Хасл");

        assertThat(request.getEmail())
                .as("Email должен быть 'test@mail.ru'")
                .isEqualTo("test@mail.ru");

        assertThat(request.getGender())
                .as("Пол должен быть 'male'")
                .isEqualTo("male");

        assertThat(request.getStatus())
                .as("Статус должен быть 'active'")
                .isEqualTo("active");
    }

    @Test
    public void userResponseGettersWork() {
        UserResponse response = new UserResponse();

        response.setId(123);
        response.setName("Хасл");
        response.setEmail("hasl@mail.ru");

        assertThat(response.getId())
                .as("ID должен быть 123")
                .isEqualTo(123);

        assertThat(response.getName())
                .as("Имя должно быть 'Хасл'")
                .isEqualTo("Хасл");

        assertThat(response.getEmail())
                .as("Email должен быть 'hasl@mail.ru'")
                .isEqualTo("hasl@mail.ru");
    }
}