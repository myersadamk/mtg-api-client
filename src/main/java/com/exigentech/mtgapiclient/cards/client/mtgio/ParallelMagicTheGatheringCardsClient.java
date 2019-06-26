package com.exigentech.mtgapiclient.cards.client.mtgio;

import static com.exigentech.mtgapiclient.cards.client.mtgio.PageHeaderIntrospector.populateLinks;

import com.exigentech.mtgapiclient.cards.client.CardsClientException;
import com.exigentech.mtgapiclient.cards.client.ParallelCardsClient;
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
public final class ParallelMagicTheGatheringCardsClient implements ParallelCardsClient {

  private final static URI BASE_URI = URI.create("https://api.magicthegathering.io/v1/cards");
  private final WebClient client;
  private final BodyParser parser;

  @Autowired
  public ParallelMagicTheGatheringCardsClient(WebClient client, BodyParser parser) {
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
    return getPage(1);
  }

  @Override
  public Mono<Page> getPage(int index) {
    return get(constructUriForPage(index));
  }

  private Mono<Page> get(final URI uri) {
    System.out.println("Getting " + uri.toString());
    final var page = ImmutablePage.builder().self(uri);
    final var publisher = client.get().uri(uri).exchange();

    return publisher
        .doOnError(error -> new CardsClientException("Call to magicthegathering.io failed", error))
        .doOnNext(spec -> populateLinks(uri, spec.headers().asHttpHeaders(), page))
        .flatMap(spec -> spec.bodyToMono(String.class))
        .map(body -> page.cards(parser.parse(RawCards.class, body).cards()).build());
  }

  private static URI constructUriForPage(final int pageNumber) {
    return UriComponentsBuilder.fromUri(BASE_URI).queryParam("page", pageNumber).build().toUri();
  }

}
