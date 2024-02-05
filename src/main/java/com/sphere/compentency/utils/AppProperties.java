package com.sphere.compentency.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
public class AppProperties {
    @Value("${kafka.input.topic}")
    private String kafkaTopic;

    @Value("${kafka.groupID}")
    private String kafkaGroupID;

    @Value("${get.hierarchy}")
    private String getHierarchyApi;

    @Value("${passbook.update.url}")
    private String passbookUpdateUrl;

    @Value("${kafka.bootstrapServers}")
    private String kafkaBootstrapServers;

    @Value("${framework.read}")
    private String frameworkRead;

}
