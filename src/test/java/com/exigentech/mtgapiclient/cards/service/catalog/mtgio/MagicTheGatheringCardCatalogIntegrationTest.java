package com.exigentech.mtgapiclient.cards.service.catalog.mtgio;

import static java.lang.String.format;
import static java.lang.System.out;

import com.exigentech.mtgapiclient.cards.service.catalog.CardCatalog;
import com.exigentech.mtgapiclient.cards.service.catalog.ImmutableCardCriteria;
import com.exigentech.mtgapiclient.cards.service.catalog.model.Color;
import com.exigentech.mtgapiclient.config.MagicTheGatheringApiConfig;
import java.util.Set;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MagicTheGatheringApiConfig.class)
//@EnabledIfEnvironmentVariable(named = "test.integration.enabled", matches = "true")
class MagicTheGatheringCardCatalogIntegrationTest {

  private final CardCatalog cardCatalog;
  private final int n;

  @Autowired
  MagicTheGatheringCardCatalogIntegrationTest(CardCatalog cardCatalog,
      @Value("${test.integration.take-n}") int n
  ) {
    this.cardCatalog = cardCatalog;
    this.n = n;
  }

  @Test
  void printCards() {
    cardCatalog.getAllCards().take(n).doOnEach(out::println).blockLast();
  }

  @Test
  void countCards() {
    out.println(
        format("Counted %d Magic: The Gathering cards (taking n: %d)", cardCatalog.getAllCards().take(n).count().block(), n)
    );
  }

  @Test
  void printCardsWithGreenColorIdentity() {
    cardCatalog.matchCards(
        ImmutableCardCriteria.builder()
            .colorIdentity(Set.of(Color.GREEN))
            .build()
    ).replay().take(n / 4).doOnEach(out::println).blockLast();
  }

  /**
   * TODO: Results in an exception without a large multiplier, presumably because none of the first N cards match. Need to drill down on it.
   */
  @Test
  @Disabled
  void printCardsWithChandraInName() {
    cardCatalog.matchCards(
        ImmutableCardCriteria.builder()
            .nameContains("Chandra")
            .build()
    ).replay().take(n / 10).doOnEach(out::println).blockLast();
  }

  @Test
  void printCardsWithTheInName() {
    cardCatalog.matchCards(
        ImmutableCardCriteria.builder()
            .nameContains("the")
            .build()
    ).take(n).doOnEach(out::println).blockLast();
  }
}