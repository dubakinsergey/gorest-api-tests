package com.hasl.gorest.config;

import org.testng.annotations.Test;

public class ConfigCheck {

    @Test
    public void checkConfig() {
        System.out.println("Base URL: " + ConfigManager.getConfig().baseUrl());
        System.out.println("Timeout: " + ConfigManager.getConfig().timeout());
        System.out.println("Token: " + ConfigManager.getConfig().apiToken());
    }
}