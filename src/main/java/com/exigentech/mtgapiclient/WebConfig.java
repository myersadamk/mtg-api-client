package com.exigentech.mtgapiclient;

import static com.fasterxml.jackson.core.JsonGenerator.Feature.IGNORE_UNKNOWN;
import static com.fasterxml.jackson.core.JsonParser.Feature.IGNORE_UNDEFINED;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig {
  @Bean
  public WebClient webClient() {
    return WebClient.builder().build();
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper()
        .configure(IGNORE_UNDEFINED, true)
        .configure(IGNORE_UNKNOWN, true)
        .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
        .registerModule(new GuavaModule());
  }
}
