package com.exigentech.mtgapiclient.card.client.mtgio;

import static com.exigentech.mtgapiclient.card.client.mtgio.PageHeaderIntrospector.getLastPageUri;
import static com.exigentech.mtgapiclient.card.client.mtgio.PageHeaderIntrospector.getNextPageUri;
import static com.google.common.base.Preconditions.checkArgument;

import com.exigentech.mtgapiclient.card.client.CardsClient;
import com.exigentech.mtgapiclient.card.client.CardsClientException;
import com.exigentech.mtgapiclient.card.client.model.ImmutablePage;
import com.exigentech.mtgapiclient.card.client.model.Page;
import com.exigentech.mtgapiclient.card.client.model.RawCards;
import com.exigentech.mtgapiclient.card.client.util.BodyParser;
import java.net.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Component
public final class MagicTheGatheringCardsClient implements CardsClient {

  private final URI baseUri;
  private final WebClient client;
  private final BodyParser parser;

  @Autowired
  public MagicTheGatheringCardsClient(@Value("${api.mtgio.cards.uri}") String baseUri, WebClient client, BodyParser parser) {
    this.baseUri = URI.create(baseUri);
    this.client = client;
    this.parser = parser;
  }


  @Override
  public Mono<Integer> getLastPageNumber() {
    return getPage(1).map(Page::lastPageNumber);
  }

  @Override
  public Mono<Page> getPage(int pageNumber) {
    checkArgument(pageNumber > 0, "The given pageNumber must be > 0 (the mtgio API is 1-based).");

    final var publisher = client.get().uri(constructUriForPage(pageNumber)).exchange();
    final var pageBuilder = ImmutablePage.builder();

    // TODO: decide on a logging framework.
    System.out.println("Getting page at index " + pageNumber);

    return publisher
        .doOnError(error -> new CardsClientException("Call to magicthegathering.io failed", error))
        .doOnNext(
            spec -> {
              final var headers = spec.headers().asHttpHeaders();

              getNextPageUri(headers)
                  .map(MagicTheGatheringCardsClient::stripPageNumberFromUri)
                  .map(pageBuilder::nextPageNumber);

              pageBuilder.lastPageNumber(
                  getLastPageUri(headers)
                      .map(MagicTheGatheringCardsClient::stripPageNumberFromUri)
                      .orElse(pageNumber)
              );
            }
        ).flatMap(spec -> spec.bodyToMono(String.class)).map(body -> pageBuilder.cards(parser.parse(RawCards.class, body).cards()).build());
  }

  private URI constructUriForPage(final int pageNumber) {
    return UriComponentsBuilder.fromUri(baseUri).queryParam("page", pageNumber).build().toUri();
  }

  private static Integer stripPageNumberFromUri(UriComponents uriComponents) {
    return Integer.valueOf(uriComponents.getQueryParams().get("page").get(0));
  }
}
