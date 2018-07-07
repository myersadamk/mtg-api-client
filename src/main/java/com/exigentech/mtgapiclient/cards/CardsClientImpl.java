package com.exigentech.mtgapiclient.cards;

import static java.util.regex.Pattern.compile;
import static org.springframework.http.HttpHeaders.LINK;

import com.exigentech.mtgapiclient.cards.model.Cards;
import com.exigentech.mtgapiclient.cards.model.ImmutablePagedCards;
import com.exigentech.mtgapiclient.cards.model.PagedCards;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public final class CardsClientImpl implements CardsClient {

  private final static URI BASE_URI = URI.create("https://api.magicthegathering.io/v1/cards");
  private final ObjectMapper mapper;
  private final WebClient client;

  @Autowired
  public CardsClientImpl(ObjectMapper mapper, WebClient client) {
    this.mapper = mapper;
    this.client = client;
  }

  @Override
  public Mono<? extends PagedCards> getFirstPage() {
    return get(BASE_URI);
  }

  @Override
  public Mono<? extends PagedCards> getNextPage(PagedCards page) {
    if (page.self().equals(page.last())) {
      throw new IllegalArgumentException();
    }
    return get(page.next());
  }

  private Mono<? extends PagedCards> get(final URI uri) {
    final var builder = ImmutablePagedCards.builder().self(uri);
    return client.get().uri(uri).exchange()
        .doOnError(RuntimeException::new)
        .doOnNext(spec -> populateLinks(spec.headers().asHttpHeaders(), builder))
        .flatMap(spec -> spec.bodyToMono(String.class))
        .map(body -> {
          try {
            final Cards marshalledJson = mapper.readValue(body, new TypeReference<Cards>() {});
            return builder.cards(marshalledJson.cards()).build();
          } catch (IOException exception) {
            throw new RuntimeException(exception);
          }
        });
  }

  // TODO: throw into another class
  private static final String LINK_RELATIONSHIP_PATTERN = "<([\\w:/\\.\\d\\?=]+)>; rel=\"%s\"";
  private static final Pattern NEXT = compile(String.format(LINK_RELATIONSHIP_PATTERN, "next"));
  private static final Pattern LAST = compile(String.format(LINK_RELATIONSHIP_PATTERN, "last"));

  private void populateLinks(final HttpHeaders headers, final ImmutablePagedCards.Builder builder) {
    builder.next(getLink(headers, NEXT)).last(getLink(headers, LAST));
  }

  private URI getLink(final HttpHeaders headers, final Pattern pattern) {
    return URI.create(
        headers.get(LINK).stream().map(content -> {
          final Matcher matcher = pattern.matcher(content);
          if (matcher.find() && matcher.groupCount() >= 1) {
            return matcher.group(1);
          }
          return null;
        }).filter(group -> group != null).findFirst().orElseThrow(RuntimeException::new)
    );
  }
}
