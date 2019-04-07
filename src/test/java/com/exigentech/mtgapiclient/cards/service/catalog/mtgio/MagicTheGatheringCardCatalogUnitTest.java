package com.exigentech.mtgapiclient.cards.service.catalog.mtgio;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import com.exigentech.mtgapiclient.cards.client.CardsClient;
import com.exigentech.mtgapiclient.cards.client.mtgio.model.ImmutableRetrievedCard;
import com.exigentech.mtgapiclient.cards.model.Card;
import com.exigentech.mtgapiclient.cards.model.CardPage;
import com.exigentech.mtgapiclient.cards.model.ImmutableCardPage;
import java.net.URI;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
class MagicTheGatheringCardCatalogUnitTest {

  @Mock
  private CardsClient cardsClient;

  @Test
  void getAllCards_singlePage() {
    final CardPage onlyPage =
        ImmutableCardPage.builder()
            .self(URI.create("http://only.com"))
            .last(URI.create("http://only.com"))
            .addCards(ImmutableRetrievedCard.builder().name("Chandra").build())
            .build();

    when(cardsClient.getFirstPage()).thenReturn(Mono.just(onlyPage));

    final List<Card> allCards =
        new MagicTheGatheringCardCatalog(cardsClient)
            .getAllCards()
            .collect(toList())
            .block();

    assertThat(allCards.size(), is(1));
    assertThat(allCards.get(0).name(), is("Chandra"));
  }

  @Test
  void getAllCards_multiplePages() {
    final CardPage firstPage =
        ImmutableCardPage.builder()
            .self(URI.create("http://self.com"))
            .next(URI.create("http://next.com"))
            .last(URI.create("http://last.com"))
            .addCards(ImmutableRetrievedCard.builder().name("Chandra").build())
            .build();

    when(cardsClient.getFirstPage()).thenReturn(Mono.just(firstPage));

    final CardPage nextPage =
        ImmutableCardPage.builder()
            .self(URI.create("http://next.com"))
            .last(URI.create("http://last.com"))
            .addCards(ImmutableRetrievedCard.builder().name("Tibalt").build())
            .build();

    when(cardsClient.getNextPage(firstPage)).thenReturn(Mono.just(nextPage));

    final List<Card> allCards =
        new MagicTheGatheringCardCatalog(cardsClient)
            .getAllCards()
            .collect(toList())
            .block();

    assertThat(allCards.size(), is(2));
    assertThat(allCards.get(0).name(), is("Chandra"));
    assertThat(allCards.get(1).name(), is("Tibalt"));
  }
}