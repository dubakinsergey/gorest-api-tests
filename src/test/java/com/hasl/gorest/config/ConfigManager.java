package com.hasl.gorest.config;

import org.aeonbits.owner.ConfigFactory;

public class ConfigManager {

    private static final ApiConfig CONFIG = ConfigFactory.create(ApiConfig.class);

    public static ApiConfig getConfig() {
        return CONFIG;
    }
}