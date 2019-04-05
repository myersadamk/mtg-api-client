package com.exigentech.mtgapiclient.cards.client.magicthegathering;

import static java.util.regex.Pattern.compile;
import static org.springframework.http.HttpHeaders.LINK;

import com.exigentech.mtgapiclient.cards.client.CardPage;
import com.exigentech.mtgapiclient.cards.client.CardsClient;
import com.exigentech.mtgapiclient.cards.client.ImmutableCardPage;
import com.exigentech.mtgapiclient.cards.client.ImmutableCardPage.Builder;
import com.exigentech.mtgapiclient.cards.client.model.Cards;
import com.exigentech.mtgapiclient.cards.client.util.BodyParser;
import java.net.URI;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public final class MagicTheGatheringCardsClient implements CardsClient {

  private final static URI BASE_URI = URI
      .create("https://api.magicthegathering.io/v1/cards?page=130");

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
        .doOnError(RuntimeException::new)
        .doOnNext(spec -> populateLinks(uri, spec.headers().asHttpHeaders(), builder))
        .flatMap(spec -> spec.bodyToMono(String.class))
        .map(body -> builder.cards(parser.parse(Cards.class, body).cards()).build());
  }

  // TODO: throw into another class
  private static final String LINK_RELATIONSHIP_PATTERN = "<([\\w:/\\.\\d\\?=]+)>; rel=\"%s\"";
  private static final Pattern NEXT = compile(String.format(LINK_RELATIONSHIP_PATTERN, "next"));
  private static final Pattern LAST = compile(String.format(LINK_RELATIONSHIP_PATTERN, "last"));

  private void populateLinks(final URI self, final HttpHeaders headers, final Builder builder) {
    builder.next(getLink(headers, NEXT));
    builder.last(getLink(headers, LAST).orElse(self));
  }

  private Optional<URI> getLink(final HttpHeaders headers, final Pattern pattern) {
    return headers.get(LINK).stream().map(content -> {
      final Matcher matcher = pattern.matcher(content);
      if (matcher.find() && matcher.groupCount() >= 1) {
        return matcher.group(1);
      }
      return null;
    }).filter(group -> group != null).findFirst().map(uri -> URI.create(uri));
  }
}