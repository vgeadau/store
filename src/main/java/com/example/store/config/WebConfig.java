package com.example.store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.Jaxb2RootElementHttpMessageConverter;

/**
 * WebConfig class used for generating XML / JSON response based on header.
 */
@Configuration
public class WebConfig {

    @Bean
    public HttpMessageConverter<Object> createXmlHttpMessageConverter() {
        return new Jaxb2RootElementHttpMessageConverter();
    }

    @Bean
    public HttpMessageConverter<Object> createJsonHttpMessageConverter() {
        return new MappingJackson2HttpMessageConverter();
    }


}