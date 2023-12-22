package com.sphere.compentencytool.kafka.consumer.api;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class SearchObject {

    private Map<String, Object> filter;
    private Map<String, Object> search;
    private Boolean keywordSearch;

}
