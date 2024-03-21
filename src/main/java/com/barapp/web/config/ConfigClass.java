package com.barapp.web.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:local.properties")
public class ConfigClass {

    @Value("${MAPS_API_KEY}")
    public String MAPS_API_KEY;

    public String getMapsApiKey() {
        return MAPS_API_KEY;
    }

    @Getter
    static ConfigClass instance;

    public ConfigClass() {
        instance = this;
    }

}
