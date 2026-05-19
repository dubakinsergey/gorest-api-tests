package com.hasl.gorest.config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config.properties")
public interface ApiConfig extends Config {

    @Key("base.url")
    String baseUrl();

    @Key("timeout")
    int timeout();

    @Key("api.token")
    String apiToken();
}