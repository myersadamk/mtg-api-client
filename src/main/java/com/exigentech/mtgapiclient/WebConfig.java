package com.exigentech.mtgapiclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.guava.GuavaDeserializers;
import com.fasterxml.jackson.datatype.guava.GuavaModule;
import java.util.function.Consumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.ipc.netty.http.client.HttpClientOptions;

@Configuration
public class WebConfig {
  @Bean
  public WebClient webClient() {
    return WebClient.builder()
        .clientConnector(
            new ReactorClientHttpConnector(HttpClientOptions.Builder::disablePool)
        ).build();
  }

  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper().registerModule(new GuavaModule());
  }
}
