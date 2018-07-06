package com.exigentech.mtgapiclient;

import static reactor.core.publisher.Flux.fromStream;
import static reactor.core.publisher.Flux.generate;
import static reactor.core.publisher.Flux.just;
import static reactor.core.publisher.Mono.empty;
import static reactor.core.publisher.Mono.fromCallable;

import com.exigentech.mtgapiclient.model.Card;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public final class CardsClientImpl implements CardsClient {

  private final ObjectMapper mapper;
  private final WebClient client;
  private final static String url = "https://api.magicthegathering.io/v1/cards";
  private String nextPage = null;
  private String lastPage = null;

  @Autowired
//  public GetCardsClientImpl(WebClient client, @Qualifier("mtgapi.baseurl") URL baseURL) {
  public CardsClientImpl(ObjectMapper mapper, WebClient client) {
    this.mapper = mapper;
    this.client = client;
  }

  @Override
  public Mono<? extends PagedCards> getFirstPage() {
    return fromCallable(() -> {
      final var builder = ImmutablePagedCards.builder().self(URI.create(url));

      client.get().uri(url).exchange().doOnNext(
          response -> {
            try {
              builder.cards(
                  Set.of(mapper.readValue(response.bodyToMono(String.class).block(), Card[].class))
              ).next(response.headers().asHttpHeaders().getLocation());
            } catch (IOException exception) {
              throw new RuntimeException();
            }
          }
      ).block();

      return builder.build();
    });
  }

  @Override
  public Mono<? extends PagedCards> getNextPage(PagedCards page) {
    if (page.self().equals(page.last())) {
      return empty();
    }

    return fromCallable(() -> {
      final var builder = ImmutablePagedCards.builder().self(page.next());

      client.get().uri(page.next()).exchange().doOnNext(
          response -> {
            try {
              builder.cards(
                  Set.of(mapper.readValue(response.bodyToMono(String.class).block(), Card[].class))
              ).next(
                  response.headers().asHttpHeaders().getLocation()
              );
            } catch (IOException exception) {
              throw new RuntimeException();
            }
          }
      ).block();

      return builder.build();
    });
  }
}
