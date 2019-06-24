package com.exigentech.mtgapiclient.cards.client.mtgio;


import com.exigentech.mtgapiclient.cards.client.model.Page;
import com.exigentech.mtgapiclient.cards.client.model.RawCard;
import com.exigentech.mtgapiclient.cards.client.util.BodyParser;
import com.exigentech.mtgapiclient.cards.service.catalog.model.Card;
import com.exigentech.mtgapiclient.config.MagicTheGatheringApiConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MagicTheGatheringApiConfig.class)
final class MagicTheGatheringCardsClientIntegrationTest {

  private final MagicTheGatheringCardsClient cardsClient;

  @Autowired
  MagicTheGatheringCardsClientIntegrationTest(WebClient client, BodyParser parser, Converter<RawCard, Card> mapper) {
    cardsClient = new MagicTheGatheringCardsClient(client, parser, mapper);
  }

  @Test
  void printCardsOnFirstPage() {
    printCardsOnPage(cardsClient.getFirstPage());
  }

  @Test
  void printCardsOnSecondPage() {
    printCardsOnPage(cardsClient.getNextPage(cardsClient.getFirstPage().block()));
  }

  @Test
  void printCardsOnLastPage() {
    printCardsOnPage(cardsClient.getLastPage(cardsClient.getFirstPage().block()));
  }

  private static void printCardsOnPage(Mono<Page> page) {
    page.block().cards().forEach(System.out::println);
  }
}