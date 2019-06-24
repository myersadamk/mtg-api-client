package com.exigentech.mtgapiclient.cards.client.mtgio;

import static com.exigentech.mtgapiclient.cards.client.mtgio.PageHeaderIntrospector.populateLinks;

import com.exigentech.mtgapiclient.cards.client.CardsClient;
import com.exigentech.mtgapiclient.cards.client.CardsClientException;
import com.exigentech.mtgapiclient.cards.client.model.ImmutablePage;
import com.exigentech.mtgapiclient.cards.client.model.Page;
import com.exigentech.mtgapiclient.cards.client.model.RawCard;
import com.exigentech.mtgapiclient.cards.client.model.RawCards;
import com.exigentech.mtgapiclient.cards.client.util.BodyParser;
import com.exigentech.mtgapiclient.cards.service.catalog.model.Card;
import com.fasterxml.jackson.core.type.TypeReference;
import java.net.URI;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public final class MagicTheGatheringCardsClient implements CardsClient {

  private final static URI BASE_URI = URI.create("https://api.magicthegathering.io/v1/cards?page=0");
  private static final TypeReference<List<Card>> CARDS_TYPE = new TypeReference<>() {
  };

  private final WebClient client;
  private final BodyParser parser;
  private final Converter<RawCard, Card> mapper;

  @Autowired
  public MagicTheGatheringCardsClient(WebClient client, BodyParser parser, Converter<RawCard, Card> mapper) {
    this.client = client;
    this.parser = parser;
    this.mapper = mapper;
  }

  @Override
  public Mono<Page> getFirstPage() {
    return get(BASE_URI);
  }

  @Override
  public Mono<Page> getNextPage(Page page) {
    if (page.self().equals(page.last())) {
      throw new IllegalArgumentException();
    }
    return page.next().map(this::get).orElse(Mono.empty());
  }

  @Override
  public Mono<Page> getLastPage(Page page) {
    if (page.self().equals(page.last())) {
      return Mono.just(page);
    }
    return get(page.last());
  }

  private Mono<Page> get(final URI uri) {
    final var page = ImmutablePage.builder().self(uri);
    final var publisher = client.get().uri(uri).exchange();

    return publisher
        // TODO: create a specific exception class
        .doOnError(error -> new CardsClientException("Call to magicthegathering.io failed", error))
        .doOnNext(spec -> populateLinks(uri, spec.headers().asHttpHeaders(), page))
        .flatMap(spec -> spec.bodyToMono(String.class))
        .map(body -> page.cards(parser.parse(RawCards.class, body).cards()).build());
  }

}
