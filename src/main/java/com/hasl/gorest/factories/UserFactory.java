package com.hasl.gorest.factories;

import com.hasl.gorest.models.UserRequest;

import java.util.UUID;

public class UserFactory {

    public static UserRequest validUser() {

        return UserRequest.builder()
                .name("Хасл Тестовый")
                .email(generateUniqueEmail())
                .gender("male")
                .status("active")
                .build();
    }

    public static UserRequest userWithEmptyName() {

        return UserRequest.builder()
                .name("")
                .email(generateUniqueEmail())
                .gender("male")
                .status("active")
                .build();
    }

    public static UserRequest userWithLongName() {

        return UserRequest.builder()
                .name("a".repeat(256))
                .email(generateUniqueEmail())
                .gender("male")
                .status("active")
                .build();
    }

    private static String generateUniqueEmail() {
        return "test_" + System.currentTimeMillis() + "_" + UUID.randomUUID() + "@example.com";
    }
}