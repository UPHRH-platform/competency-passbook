package com.sphere.compentency.common.utils;

import org.springframework.stereotype.Component;

@Component
public class propertiesCache {
    public String getProperty(String key) {
        String value = System.getenv(key);
        if (value == null) {
            throw new IllegalStateException("Required property not set: " + key);
        }
        return value;
    }
}