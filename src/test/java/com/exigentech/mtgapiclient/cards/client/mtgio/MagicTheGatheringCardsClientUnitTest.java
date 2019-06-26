//package com.exigentech.mtgapiclient.cards.client.mtgio;
//
//import static java.lang.String.format;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.when;
//
//import com.exigentech.mtgapiclient.cards.client.model.ImmutableRawCard;
//import com.exigentech.mtgapiclient.cards.client.model.ImmutableRawCards;
//import com.exigentech.mtgapiclient.cards.client.model.Page;
//import com.exigentech.mtgapiclient.cards.client.model.RawCards;
//import com.exigentech.mtgapiclient.cards.client.util.BodyParser;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.common.collect.ImmutableSet;
//import java.net.URI;
//import java.util.Optional;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.json.JacksonTester;
//import org.springframework.core.ResolvableType;
//import org.springframework.http.HttpHeaders;
//import org.springframework.web.reactive.function.client.ClientResponse;
//import org.springframework.web.reactive.function.client.ClientResponse.Headers;
//import org.springframework.web.reactive.function.client.WebClient;
//import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
//import reactor.core.publisher.Mono;
//
//@ExtendWith(MockitoExtension.class)
//class MagicTheGatheringCardsClientUnitTest {
//
//  private final BodyParser bodyParser;
//  private final WebClient webClient;
//
//  private MagicTheGatheringCardsClient magicTheGatheringCardsClient;
//
//  private final JacksonTester<RawCards> rawCardsJSON =
//      new JacksonTester<>(getClass(), ResolvableType.forClass(RawCards.class), new ObjectMapper());
//
//  MagicTheGatheringCardsClientUnitTest(@Mock BodyParser bodyParser, @Mock WebClient webClient) {
//    magicTheGatheringCardsClient = new MagicTheGatheringCardsClient(webClient, bodyParser);
//    this.bodyParser = bodyParser;
//    this.webClient = webClient;
//  }
//
//  @Test
//  void getAllCards_singlePage(@Mock RequestHeadersUriSpec spec, @Mock ClientResponse response, @Mock Headers headers) throws Exception {
//    final URI lastPageURI = URI.create("https://api.magicthegathering.io/v1/cards?page=462");
//    final URI nextPageURI = URI.create("https://api.magicthegathering.io/v1/cards?page=2");
//    final URI selfPageURI = URI.create("https://api.magicthegathering.io/v1/cards?page=0");
//
//    when(headers.asHttpHeaders()).then((mock) -> {
//      final HttpHeaders httpHeaders = new HttpHeaders();
//      httpHeaders.add("link", format("<%s>; rel=\"last\", <%s>; rel=\"next\"", lastPageURI.toString(), nextPageURI.toString()));
//      return httpHeaders;
//    });
//    when(response.headers()).thenReturn(headers);
//
//    final RawCards cards = createCards();
//    final String responseBody = rawCardsJSON.write(cards).getJson();
//
//    when(response.bodyToMono(String.class)).thenReturn(Mono.just(responseBody));
//    when(bodyParser.parse(RawCards.class, responseBody)).thenReturn(cards);
//
//    configureWebClientMocks(spec, response);
//
//    final Page firstPage = magicTheGatheringCardsClient.getFirstPage().block();
//    assertThat(firstPage.last()).isEqualTo(lastPageURI);
//    assertThat(firstPage.next()).isEqualTo(Optional.of(nextPageURI));
//    assertThat(firstPage.self()).isEqualTo(selfPageURI);
//    assertThat(firstPage.cards()).isEqualTo(cards.cards());
//  }
//
//  private void configureWebClientMocks(RequestHeadersUriSpec spec, ClientResponse response) {
//    when(webClient.get()).thenReturn(spec);
//    when(spec.uri(any(URI.class))).thenReturn(spec);
//    when(spec.exchange()).thenReturn(Mono.just(response));
//  }
//
//  private static RawCards createCards() {
//    return ImmutableRawCards.of(
//        ImmutableSet.of(
//            ImmutableRawCard.builder()
//                .name("Flamewave Invoker")
//                .rarity("Uncommon")
//                .cmc(3.0)
//                .text("{7}{R}: Flamewave Invoker deals 5 damage to target player or planeswalker.")
//                .manaCost("{2}{R}")
//                .colorIdentity(ImmutableSet.of("R"))
//                .colors(ImmutableSet.of("Red"))
//                .types(ImmutableSet.of("Creature"))
//                .build()
//        )
//    );
//  }
//}