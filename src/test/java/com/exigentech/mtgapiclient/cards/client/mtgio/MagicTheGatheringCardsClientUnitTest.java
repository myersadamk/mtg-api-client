package com.exigentech.mtgapiclient.cards.client.mtgio;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.exigentech.mtgapiclient.cards.client.mtgio.model.Cards;
import com.exigentech.mtgapiclient.cards.client.mtgio.model.ImmutableCards;
import com.exigentech.mtgapiclient.cards.client.mtgio.model.ImmutableRetrievedCard;
import com.exigentech.mtgapiclient.cards.client.util.BodyParser;
import com.exigentech.mtgapiclient.cards.model.CardPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.RequestHeadersUriSpec;

@Disabled("Lazy - maybe refactoring is needed, because this is a pain to test")
@ExtendWith(MockitoExtension.class)
class MagicTheGatheringCardsClientUnitTest {

  @Mock
  private BodyParser bodyParser;

  @Mock
  private WebClient webClient;

  private MagicTheGatheringCardsClient magicTheGatheringCardsClient;

  @BeforeEach
  void setup() {
    magicTheGatheringCardsClient = new MagicTheGatheringCardsClient(bodyParser, webClient);
  }

  @Test
  void getAllCards_singlePage(@Mock RequestHeadersUriSpec spec) {
    final Cards cards =
        ImmutableCards.builder()
            .addCards(ImmutableRetrievedCard.builder().name("Chandra").build())
            .build();

    when(spec.headers(
        (s) -> {
         final HttpHeaders httpHeaders = new HttpHeaders();
         httpHeaders.add("next", "http://next; rel=next");
         httpHeaders.add("last", "http://last; rel=last");
        })
    );
    when(webClient.get()).thenReturn(spec);
    when(bodyParser.parse(Cards.class, anyString())).thenReturn(cards);
    final CardPage firstPage = magicTheGatheringCardsClient.getFirstPage().block();
  }
}