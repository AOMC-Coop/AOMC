package com.aomc.coop.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import com.fasterxml.jackson.annotation.JsonInclude;

@Configuration // 스프링 설정 클래스를 선언하는 어노테이션
class JsonConfig {

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        // ObjectMapper provides functionality for reading and writing JSON, either to and from basic POJOs (Plain Old Java Objects), or to and from a general-purpose JSON Tree Model
        // as well as related functionality for performing conversions.
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        // Jackson2ObjectMapperBuilder : Set a custom inclusion strategy for serialization.
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);
        builder.serializationInclusion(JsonInclude.Include.NON_EMPTY);
        builder.failOnUnknownProperties(false);
        return builder;
    }
}
