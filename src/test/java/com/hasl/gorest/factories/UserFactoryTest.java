package com.hasl.gorest.factories;

import com.hasl.gorest.models.UserRequest;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserFactoryTest {

    @Test
    public void validUserShouldHaveAllFields() {

        UserRequest user = UserFactory.validUser();

        assertThat(user.getName())
                .as("Имя должно быть 'Хасл Тестовый'")
                .isEqualTo("Хасл Тестовый");

        assertThat(user.getEmail())
                .as("Email должен содержать @example.com")
                .endsWith("@example.com");

        assertThat(user.getGender())
                .as("Пол должен быть 'male'")
                .isEqualTo("male");

        assertThat(user.getStatus())
                .as("Статус должен быть 'active'")
                .isEqualTo("active");
    }

    @Test
    public void userWithEmptyNameShouldHaveEmptyName() {

        UserRequest user = UserFactory.userWithEmptyName();

        assertThat(user.getName())
                .as("Имя должно быть пустым")
                .isEmpty();

        assertThat(user.getEmail())
                .as("Email не должен быть пустым")
                .isNotEmpty();

    }

    @Test
    public void userWithLongNameShouldHaveLength256() {

        UserRequest user = UserFactory.userWithLongName();

        assertThat(user.getName())
                .as("Длина имени должна быть 256 символов")
                .hasSize(256);
    }
}