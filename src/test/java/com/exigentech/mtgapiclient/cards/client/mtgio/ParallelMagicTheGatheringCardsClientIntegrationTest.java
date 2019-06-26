package com.exigentech.mtgapiclient.cards.client.mtgio;


import com.exigentech.mtgapiclient.cards.client.model.Page;
import com.exigentech.mtgapiclient.cards.client.util.BodyParser;
import com.exigentech.mtgapiclient.config.MagicTheGatheringApiConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MagicTheGatheringApiConfig.class)
final class ParallelMagicTheGatheringCardsClientIntegrationTest {

  private final ParallelMagicTheGatheringCardsClient cardsClient;

  @Autowired
  ParallelMagicTheGatheringCardsClientIntegrationTest(WebClient client, BodyParser parser) {
    cardsClient = new ParallelMagicTheGatheringCardsClient(client, parser);
  }

  @Test
  void getCardPages() {
    cardsClient.getPageCount().doOnNext(System.out::println).block();
  }

  @Test
  void printCardsOnFirstPage() {
    printCardsOnPage(cardsClient.getPage(3));
  }

  @Test
  void printCardsOnSecondPage() {
    printCardsOnPage(cardsClient.getPage(2));
  }
//
//  @Test
//  void printCardsOnLastPage() {
//    printCardsOnPage(cardsClient.getLastPage(cardsClient.getFirstPage().block()));
//  }

  private static void printCardsOnPage(Mono<Page> page) {
    page.block().cards().forEach(System.out::println);
  }
}