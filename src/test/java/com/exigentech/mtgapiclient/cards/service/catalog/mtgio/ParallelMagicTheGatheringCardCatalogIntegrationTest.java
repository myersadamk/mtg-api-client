package com.exigentech.mtgapiclient.cards.service.catalog.mtgio;

import static java.lang.String.format;
import static java.lang.System.out;

import com.exigentech.mtgapiclient.cards.client.ParallelCardsClient;
import com.exigentech.mtgapiclient.cards.client.model.RawCard;
import com.exigentech.mtgapiclient.cards.service.catalog.ImmutableCardCriteria;
import com.exigentech.mtgapiclient.cards.service.catalog.model.Card;
import com.exigentech.mtgapiclient.cards.service.catalog.model.Color;
import com.exigentech.mtgapiclient.config.MagicTheGatheringApiConfig;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.converter.Converter;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MagicTheGatheringApiConfig.class)
//@EnabledIfEnvironmentVariable(named = "test.integration.enabled", matches = "true")
class ParallelMagicTheGatheringCardCatalogIntegrationTest {

  private final ParallelMagicTheGatheringCardCatalog cardCatalog;
  private final int cardRetrievalBase;
  private final int cardRetrievalMax;

  @Autowired
  ParallelMagicTheGatheringCardCatalogIntegrationTest(
      ParallelCardsClient cardsClient,
      Converter<RawCard, Card> mapper,
      @Value("${test.integration.card-retrieval-base}") int cardRetrievalBase,
      @Value("${test.integration.card-retrieval-max}") int cardRetrievalMax
  ) {
    cardCatalog = new ParallelMagicTheGatheringCardCatalog(cardsClient, mapper);
    this.cardRetrievalBase = cardRetrievalBase;
    this.cardRetrievalMax = cardRetrievalMax;
  }

  @Test
  void printCards() {
    cardCatalog.getAllCards().take(cardRetrievalBase).doOnEach(out::println).blockLast();
  }

  @Test
  void countCards() {
    out.println(
        format(
            "Counted %d Magic: The Gathering cards (base retrieval limit: %d)",
            cardCatalog.getAllCards().take(cardRetrievalBase).count().block(),
            cardRetrievalBase
        )
    );
  }

  @Test
  void printCardsWithGreenColorIdentity() {
    System.out.println(cardRetrievalBase);
    cardCatalog.matchCards(
        ImmutableCardCriteria.builder()
            .colorIdentity(Set.of(Color.GREEN))
            .build()
    ).take(101).doOnEach(out::println).blockLast();
  }

  /**
   * TODO: Results in an exception without a large multiplier, presumably because none of the first N cards match. Need to drill down on it.
   */
  @Test
  void printCardsWithChandraInName() {
    cardCatalog.matchCards(
        ImmutableCardCriteria.builder()
            .nameContains("Chandra")
            .build()
    ).take(calculateTakeForMultiplier(20)).doOnEach(out::println).blockLast();
  }

  @Test
  void printCardsWithTheInName() {
    cardCatalog.matchCards(
        ImmutableCardCriteria.builder()
            .nameContains("the")
            .build()
    ).take(cardRetrievalBase).doOnEach(out::println).blockLast();
  }

  /**
   * In the event that a criteria is so specific that it requires more than the {@code cardRetrievalBase} cards in order to match, this can be used
   * to calculate a larger take count that is more likely to match, but does not exceed {@code cardRetrievalMax}.
   *
   * @return {@code multiplier * cardRetrievalBase} if that value is {@code < cardRetrievalMax}, {@code cardRetrievalMax} otherwise.
   */
  private int calculateTakeForMultiplier(int multiplier) {
    final int multiplierAppliedToBase = cardRetrievalBase * multiplier;
    return multiplierAppliedToBase > cardRetrievalMax ? cardRetrievalMax : multiplierAppliedToBase;
  }
}