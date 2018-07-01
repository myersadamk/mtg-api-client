package com.exigentech.mtgapiclient;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class CardsClientImplTest {
  @Test
  void lol(@Autowired WebClient client, @Autowired ObjectMapper mapper) {
    new CardsClientImpl(mapper, client).getAll().subscribe(System.out::println);
  }
}