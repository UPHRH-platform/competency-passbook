package com.sphere.compentencytool.common.utils;


import org.apache.commons.lang3.StringUtils;


public class propertiesCache {
    public String getProperty(String key) {
        String value = System.getenv(key);
//        if (StringUtils.isNotBlank(value));
        return value;
    }
}