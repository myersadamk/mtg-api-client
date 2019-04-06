package com.exigentech.mtgapiclient.cards.service.mtgio;

import static java.lang.String.format;
import static java.lang.System.out;

import com.exigentech.mtgapiclient.cards.client.CardsClient;
import com.exigentech.mtgapiclient.config.MagicTheGatheringApiConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MagicTheGatheringApiConfig.class)
class MagicTheGatheringCardCatalogIntegrationTest {

  @Test
  void printAllCards(@Autowired CardsClient cardsClient) {
    new MagicTheGatheringCardCatalog(cardsClient).getAllCards().doOnEach(out::println).blockLast();
  }

  @Test
  void countAllCards(@Autowired CardsClient cardsClient) {
    out.println(
        format(
            "The total amount of Magic: The Gathering cards is %d",
            new MagicTheGatheringCardCatalog(cardsClient).getAllCards().count().block()
        )
    );
  }
}