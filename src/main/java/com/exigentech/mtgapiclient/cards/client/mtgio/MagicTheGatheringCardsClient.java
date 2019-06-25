package com.exigentech.mtgapiclient.cards.client.mtgio;

import static com.exigentech.mtgapiclient.cards.client.mtgio.PageHeaderIntrospector.populateLinks;

import com.exigentech.mtgapiclient.cards.client.CardsClient;
import com.exigentech.mtgapiclient.cards.client.CardsClientException;
import com.exigentech.mtgapiclient.cards.client.model.ImmutablePage;
import com.exigentech.mtgapiclient.cards.client.model.Page;
import com.exigentech.mtgapiclient.cards.client.model.RawCards;
import com.exigentech.mtgapiclient.cards.client.util.BodyParser;
import java.net.URI;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Component
public final class MagicTheGatheringCardsClient implements CardsClient {

  private final static URI BASE_URI = URI.create("https://api.magicthegathering.io/v1/cards?page=1");
  private final WebClient client;
  private final BodyParser parser;

  @Autowired
  public MagicTheGatheringCardsClient(WebClient client, BodyParser parser) {
    this.client = client;
    this.parser = parser;
    UriComponentsBuilder.fromUri(BASE_URI).queryParam("page", "1");
  }

  public Mono<Integer> getPageCount() {
    return getFirstPage().cache(Duration.ofHours(4)).map(page -> {
      final String lastPageUriQuery = page.last().getQuery();
      return Integer.valueOf(lastPageUriQuery.substring(lastPageUriQuery.indexOf('=') + 1));
    });
  }

  @Override
  public Mono<Page> getFirstPage() {
    return get(BASE_URI);
  }

  @Override
  public Mono<Page> getNextPage(Page page) {
    return page.next().map(this::get).orElse(Mono.empty());
  }

  @Override
  public Mono<Page> getLastPage(Page page) {
    return get(page.last());
  }

  private Mono<Page> get(final URI uri) {
    System.out.println("Getting " + uri.toString());
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
