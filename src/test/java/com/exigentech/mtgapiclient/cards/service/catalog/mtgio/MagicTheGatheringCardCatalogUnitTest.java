package com.exigentech.mtgapiclient.cards.service.catalog.mtgio;

import static com.exigentech.mtgapiclient.cards.TestData.*;
import static org.mockito.Mockito.when;

import com.exigentech.mtgapiclient.cards.TestData;
import com.exigentech.mtgapiclient.cards.client.CardsClient;
import com.exigentech.mtgapiclient.cards.client.model.ImmutablePage;
import com.exigentech.mtgapiclient.cards.service.catalog.CardCatalog;
import com.exigentech.mtgapiclient.cards.service.catalog.ImmutableCardCriteria;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

// TODO: lots of redundant setup in these tests; can be refactored to be more terse.
@ExtendWith(MockitoExtension.class)
class MagicTheGatheringCardCatalogUnitTest {

  private final CardsClient cardsClient;
  private final CardCatalog cardCatalog;

  MagicTheGatheringCardCatalogUnitTest(@Mock CardsClient cardsClient) {
    cardCatalog = new MagicTheGatheringCardCatalog(cardsClient, TestData.MAPPER);
    this.cardsClient = cardsClient;
  }

  @Nested
  class SinglePage {

    @Test
    void getAllCards() {
      when(cardsClient.getPage(1)).thenReturn(Mono.just(ImmutablePage.builder()
          .nextPageNumber(Optional.empty())
          .lastPageNumber(1)
          .addCards(RAW_FLAMEWAVE_INVOKER, RAW_CANOPY_SPIDER)
          .build())
      );

      StepVerifier.create(cardCatalog.getAllCards())
          .expectNext(MAPPED_FLAMEWAVE_INVOKER)
          .expectNext(MAPPED_CANOPY_SPIDER)
          .verifyComplete();
    }
  }

  @Nested
  class MultiplePages {

    @BeforeEach
    void setup() {
      when(cardsClient.getPage(1)).thenReturn(Mono.just(ImmutablePage.builder()
          .nextPageNumber(2)
          .lastPageNumber(2)
          .addCards(RAW_FLAMEWAVE_INVOKER)
          .build())
      );

      when(cardsClient.getPage(2)).thenReturn(Mono.just(ImmutablePage.builder()
          .nextPageNumber(Optional.empty())
          .lastPageNumber(2)
          .addCards(RAW_CANOPY_SPIDER)
          .build())
      );
    }

    @Test
    void getAllCards() {
      StepVerifier.create(cardCatalog.getAllCards())
          .expectNext(MAPPED_FLAMEWAVE_INVOKER)
          .expectNext(MAPPED_CANOPY_SPIDER)
          .expectComplete();
    }

    @Test
    void matchCards_byCardName_allMatch() {
      StepVerifier.create(cardCatalog.matchCards(ImmutableCardCriteria.builder().nameContains("o").build()))
          .expectNext(MAPPED_FLAMEWAVE_INVOKER)
          .expectNext(MAPPED_CANOPY_SPIDER)
          .expectComplete();
    }

    @Test
    void matchCards_byCardName_oneMatches() {
      StepVerifier.create(cardCatalog.matchCards(ImmutableCardCriteria.builder().nameContains("flame").build()))
          .expectNext(MAPPED_FLAMEWAVE_INVOKER)
          .expectComplete();
    }
  }
}