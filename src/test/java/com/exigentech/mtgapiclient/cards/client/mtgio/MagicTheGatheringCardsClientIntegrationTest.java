package com.exigentech.mtgapiclient.cards.client.mtgio;


import com.exigentech.mtgapiclient.cards.client.util.BodyParser;
import com.exigentech.mtgapiclient.config.MagicTheGatheringApiConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MagicTheGatheringApiConfig.class)
final class MagicTheGatheringCardsClientIntegrationTest {

  @Test
  void printCardsOnFirstPage(@Autowired WebClient client, @Autowired BodyParser parser) {
    new MagicTheGatheringCardsClient(parser, client).getFirstPage().block().cards().forEach(System.out::println);
  }
}