package com.exigentech.mtgapiclient.card.client.mtgio;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MagicTheGatheringCardsClientUnitTest {

  // TODO: Fix these once the URI stuff has been appropriately extracted into another class.

//  private final BodyParser bodyParser;
//  private final WebClient webClient;
//
//  private CardsClient cardsClient;
//
//  private final JacksonTester<RawCards> rawCardsJSON =
//      new JacksonTester<>(getClass(), ResolvableType.forClass(RawCards.class), new ObjectMapper());
//
//  MagicTheGatheringCardsClientUnitTest(@Mock BodyParser bodyParser, @Mock WebClient webClient) {
//    cardsClient = new MagicTheGatheringCardsClient(webClient, bodyParser);
//    this.bodyParser = bodyParser;
//    this.webClient = webClient;
//  }
//
//  @Nested
//  class SinglePage {
//    @Test
//    void getAllCards_singlePage(@Mock RequestHeadersUriSpec spec, @Mock ClientResponse response, @Mock Headers headers) throws Exception {
//      final URI lastPageURI = URI.create("https://api.magicthegathering.io/v1/cards?page=462");
//      final URI nextPageURI = URI.create("https://api.magicthegathering.io/v1/cards?page=2");
//      final URI selfPageURI = URI.create("https://api.magicthegathering.io/v1/cards?page=0");
//
//      when(headers.asHttpHeaders()).then((mock) -> {
//        final HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add("link", format("<%s>; rel=\"last\", <%s>; rel=\"next\"", lastPageURI.toString(), nextPageURI.toString()));
//        return httpHeaders;
//      });
//      when(response.headers()).thenReturn(headers);
//
//      final RawCards cards = createCards();
//      final String responseBody = rawCardsJSON.write(cards).getJson();
//
//      when(response.bodyToMono(String.class)).thenReturn(Mono.just(responseBody));
//      when(bodyParser.parse(RawCards.class, responseBody)).thenReturn(cards);
//
//      configureWebClientMocks(spec, response);
//
//      final Page firstPage = cardsClient.getFirstPage().block();
//      assertThat(firstPage.last()).isEqualTo(lastPageURI);
//      assertThat(firstPage.next()).isEqualTo(Optional.of(nextPageURI));
//      assertThat(firstPage.self()).isEqualTo(selfPageURI);
//      assertThat(firstPage.cards()).isEqualTo(cards.cards());
//    }
//  }
//
//  private void configureWebClientMocks(RequestHeadersUriSpec spec, ClientResponse response) {
//    when(webClient.get()).thenReturn(spec);
//    when(spec.uri(any(URI.class))).thenReturn(spec);
//    when(spec.exchange()).thenReturn(Mono.just(response));
//  }
//
//  private static RawCards createCards(RawCard... rawCard) {
//    return ImmutableRawCards.of(Set.of(rawCard));
//  }
}