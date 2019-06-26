//package com.exigentech.mtgapiclient.cards.service.catalog.mtgio;
//
//import static java.util.stream.Collectors.toList;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.Mockito.when;
//
//import com.exigentech.mtgapiclient.cards.client.CardsClient;
//import com.exigentech.mtgapiclient.cards.client.model.ImmutablePage;
//import com.exigentech.mtgapiclient.cards.client.model.ImmutableRawCard;
//import com.exigentech.mtgapiclient.cards.client.model.Page;
//import com.exigentech.mtgapiclient.cards.client.model.RawCard;
//import com.exigentech.mtgapiclient.cards.service.catalog.ImmutableCardCriteria;
//import com.exigentech.mtgapiclient.cards.service.catalog.mapping.RawCardToCardMapper;
//import com.google.common.collect.ImmutableSet;
//import java.net.URI;
//import java.util.List;
//import java.util.stream.Stream;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import reactor.core.publisher.Mono;
//
//// TODO: lots of redundant setup in these tests; can be refactored to be more terse.
//@ExtendWith(MockitoExtension.class)
//class MagicTheGatheringCardCatalogUnitTest {
//
//  private final CardsClient cardsClient;
//  private final RawCardToCardMapper mapper = new RawCardToCardMapper();
//  private final MagicTheGatheringCardCatalog cardCatalog;
//
//  MagicTheGatheringCardCatalogUnitTest(@Mock CardsClient cardsClient) {
//    cardCatalog = new MagicTheGatheringCardCatalog(cardsClient, mapper);
//    this.cardsClient = cardsClient;
//  }
//
//  @Test
//  void getAllCards_singlePage() {
//    final Page onlyPage =
//        ImmutablePage.builder()
//            .self(URI.create("http://only.com"))
//            .last(URI.create("http://only.com"))
//            .addCards(FLAMEWAVE_INVOKER, CANOPY_SPIDER)
//            .build();
//
//    when(cardsClient.getFirstPage()).thenReturn(Mono.just(onlyPage));
//
//    assertThat(
//        cardCatalog.getAllCards().collect(toList()).block()
//    ).isEqualTo(
//        Stream.of(FLAMEWAVE_INVOKER, CANOPY_SPIDER).map(mapper::convert).collect(toList())
//    );
//  }
//
//  @Test
//  void getAllCards_multiplePages() {
//    final Page firstPage =
//        ImmutablePage.builder()
//            .self(URI.create("http://self.com"))
//            .next(URI.create("http://last.com"))
//            .last(URI.create("http://last.com"))
//            .addCards(FLAMEWAVE_INVOKER)
//            .build();
//
//    when(cardsClient.getFirstPage()).thenReturn(Mono.just(firstPage));
//
//    final Page nextPage =
//        ImmutablePage.builder()
//            .self(URI.create("http://last.com"))
//            .last(URI.create("http://last.com"))
//            .addCards(CANOPY_SPIDER)
//            .build();
//
//    when(cardsClient.getNextPage(firstPage)).thenReturn(Mono.just(nextPage));
//
//    assertThat(
//        cardCatalog.getAllCards().collect(toList()).block()
//    ).isEqualTo(
//        Stream.of(FLAMEWAVE_INVOKER, CANOPY_SPIDER).map(mapper::convert).collect(toList())
//    );
//  }
//
//  @Test
//  void matchCards_byCardName_allMatch() {
//    final Page firstPage =
//        ImmutablePage.builder()
//            .self(URI.create("http://self.com"))
//            .next(URI.create("http://last.com"))
//            .last(URI.create("http://last.com"))
//            .addCards(FLAMEWAVE_INVOKER)
//            .build();
//
//    when(cardsClient.getFirstPage()).thenReturn(Mono.just(firstPage));
//
//    final Page nextPage =
//        ImmutablePage.builder()
//            .self(URI.create("http://last.com"))
//            .last(URI.create("http://last.com"))
//            .addCards(CANOPY_SPIDER)
//            .build();
//
//    when(cardsClient.getNextPage(firstPage)).thenReturn(Mono.just(nextPage));
//
//    assertThat(
//        cardCatalog.matchCards(ImmutableCardCriteria.builder().nameContains("o").build()).collect(toList()).block()
//    ).isEqualTo(
//        Stream.of(FLAMEWAVE_INVOKER, CANOPY_SPIDER).map(mapper::convert).collect(toList())
//    );
//  }
//
//  @Test
//  void matchCards_byCardName_oneMatches() {
//    final Page firstPage =
//        ImmutablePage.builder()
//            .self(URI.create("http://self.com"))
//            .next(URI.create("http://last.com"))
//            .last(URI.create("http://last.com"))
//            .addCards(FLAMEWAVE_INVOKER)
//            .build();
//
//    when(cardsClient.getFirstPage()).thenReturn(Mono.just(firstPage));
//
//    final Page nextPage =
//        ImmutablePage.builder()
//            .self(URI.create("http://last.com"))
//            .last(URI.create("http://last.com"))
//            .addCards(CANOPY_SPIDER)
//            .build();
//
//    when(cardsClient.getNextPage(firstPage)).thenReturn(Mono.just(nextPage));
//
//    assertThat(
//        cardCatalog.matchCards(ImmutableCardCriteria.builder().nameContains(FLAMEWAVE_INVOKER.name().substring(2, 5)).build()).collect(toList()).block()
//    ).isEqualTo(List.of(mapper.convert(FLAMEWAVE_INVOKER)));
//  }
//
//  // TODO: May as well gather some actual card data and parse it into objects using JacksonTester, since this is needed in multiple places.
//  private static final RawCard FLAMEWAVE_INVOKER =
//      ImmutableRawCard.builder()
//          .name("Flamewave Invoker")
//          .rarity("Uncommon")
//          .cmc(3.0)
//          .text("{7}{R}: Flamewave Invoker deals 5 damage to target player or planeswalker.")
//          .manaCost("{2}{R}")
//          .colorIdentity(ImmutableSet.of("R"))
//          .colors(ImmutableSet.of("Red"))
//          .types(ImmutableSet.of("Creature"))
//          .build();
//
//  private static final RawCard CANOPY_SPIDER =
//      ImmutableRawCard.builder()
//          .name("Canopy Spider")
//          .rarity("Common")
//          .cmc(2.0)
//          .text("Reach (This creature can block creatures with flying.)")
//          .manaCost("{1}{G}")
//          .colorIdentity(ImmutableSet.of("G"))
//          .colors(ImmutableSet.of("Green"))
//          .types(ImmutableSet.of("Creature"))
//          .build();
//}