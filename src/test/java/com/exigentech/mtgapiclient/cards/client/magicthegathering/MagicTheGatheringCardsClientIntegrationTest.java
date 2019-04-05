package com.exigentech.mtgapiclient.cards.client.magicthegathering;



import com.exigentech.mtgapiclient.cards.client.util.BodyParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class MagicTheGatheringCardsClientIntegrationTest {

  @Test
  void printCards(@Autowired WebClient client, @Autowired BodyParser parser) {
    new MagicTheGatheringCardsClient(parser, client).getFirstPage().block().cards().forEach(System.out::println);
  }
}