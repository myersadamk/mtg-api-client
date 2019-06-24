package com.exigentech.mtgapiclient.cards.client.mtgio;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.exigentech.mtgapiclient.cards.client.mtgio.model.Cards;
import com.exigentech.mtgapiclient.cards.client.mtgio.model.ImmutableCards;
import com.exigentech.mtgapiclient.cards.client.util.BodyParser;
import com.exigentech.mtgapiclient.cards.model.Card;
import com.exigentech.mtgapiclient.cards.model.CardPage;
import com.exigentech.mtgapiclient.cards.model.ImmutableCard;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ClientResponse.Headers;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;
import reactor.core.publisher.Mono;

@Disabled("Lazy - maybe refactoring is needed, because this is a pain to test")
@ExtendWith(MockitoExtension.class)
class MagicTheGatheringCardsClientUnitTest {

  @Mock
  private BodyParser bodyParser;

  @Mock
  private WebClient webClient;

  private MagicTheGatheringCardsClient magicTheGatheringCardsClient;

  private JacksonTester<Cards> cardsJSON;

  @BeforeEach
  void setup() {
    magicTheGatheringCardsClient = new MagicTheGatheringCardsClient(bodyParser, webClient);
    JacksonTester.initFields(this, new ObjectMapper());
  }

  @Test
  void getAllCards_singlePage(@Mock RequestHeadersUriSpec spec, @Mock ClientResponse response, @Mock Headers headers) throws Exception {
    final URI lastPageURI = URI.create("https://api.magicthegathering.io/v1/cards?page=462");
    final URI nextPageURI = URI.create("https://api.magicthegathering.io/v1/cards?page=2");
    final URI selfPageURI = URI.create("https://api.magicthegathering.io/v1/cards?page=0");

    when(headers.asHttpHeaders()).then((mock) -> {
      final HttpHeaders httpHeaders = new HttpHeaders();
      httpHeaders.add("link", format("<%s>; rel=\"last\", <%s>; rel=\"next\"", lastPageURI.toString(), nextPageURI.toString()));
      return httpHeaders;
    });
    when(response.headers()).thenReturn(headers);

    final Cards cards = createCards();
    final String responseBody = cardsJSON.write(cards).getJson();

    when(response.bodyToMono(String.class)).thenReturn(Mono.just(responseBody));
    when(bodyParser.parse(Cards.class, responseBody)).thenReturn(cards);

    configureWebClientMocks(spec, response, headers);

    final CardPage firstPage = magicTheGatheringCardsClient.getFirstPage().block();
    assertThat(firstPage.last()).isEqualTo(lastPageURI);
    assertThat(firstPage.next()).isEqualTo(Optional.of(nextPageURI));
    assertThat(firstPage.self()).isEqualTo(selfPageURI);
    assertThat(firstPage.cards()).isEqualTo(cards.cards());
  }

  private void configureWebClientMocks(RequestHeadersUriSpec spec, ClientResponse response, Headers headers
  ) {
    when(webClient.get()).thenReturn(spec);
    when(spec.uri(any(URI.class))).thenReturn(spec);
    when(spec.exchange()).thenReturn(Mono.just(response));
  }

  private static Cards createCards() {
    return ImmutableCards.of(
        ImmutableSet.of(
            ImmutableCard.builder()
                .name("Chandra")
                .build()
        )
    );
  }
}