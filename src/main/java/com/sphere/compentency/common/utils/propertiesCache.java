package com.sphere.compentency.common.utils;


public class propertiesCache {
    public String getProperty(String key) {
        String value = System.getenv(key);
//        if (StringUtils.isNotBlank(value));
        return value;
    }
}