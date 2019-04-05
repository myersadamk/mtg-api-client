package com.exigentech.mtgapiclient.cards.client.mtgio;

import static com.exigentech.mtgapiclient.cards.client.mtgio.PageHeaderIntrospector.populateLinks;

import com.exigentech.mtgapiclient.cards.client.CardsClient;
import com.exigentech.mtgapiclient.cards.client.CardsClientException;
import com.exigentech.mtgapiclient.cards.client.mtgio.model.Cards;
import com.exigentech.mtgapiclient.cards.client.util.BodyParser;
import com.exigentech.mtgapiclient.cards.model.CardPage;
import com.exigentech.mtgapiclient.cards.model.ImmutableCardPage;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public final class MagicTheGatheringCardsClient implements CardsClient {

  private final static URI BASE_URI = URI.create("https://api.magicthegathering.io/v1/cards?page=1");

  private final BodyParser parser;
  private final WebClient client;

  @Autowired
  public MagicTheGatheringCardsClient(BodyParser parser, WebClient client) {
    this.parser = parser;
    this.client = client;
  }

  @Override
  public Mono<CardPage> getFirstPage() {
    return get(BASE_URI);
  }

  @Override
  public Mono<CardPage> getNextPage(CardPage page) {
    if (page.self().equals(page.last())) {
      throw new IllegalArgumentException();
    }
    return page.next().map(this::get).orElse(Mono.empty());
  }

  private Mono<CardPage> get(final URI uri) {
    final var builder = ImmutableCardPage.builder().self(uri);
    final var publisher = client.get().uri(uri).exchange();

    return publisher
        // TODO: create a specific exception class
        .doOnError(error -> new CardsClientException("Call failed to magicthegatheriong.io", error))
        .doOnNext(spec -> populateLinks(uri, spec.headers().asHttpHeaders(), builder))
        .flatMap(spec -> spec.bodyToMono(String.class))
        .map(body -> builder.cards(parser.parse(Cards.class, body).cards()).build());
  }
}
