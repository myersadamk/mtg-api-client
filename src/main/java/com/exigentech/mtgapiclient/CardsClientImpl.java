package com.exigentech.mtgapiclient;

import static reactor.core.publisher.Flux.empty;
import static reactor.core.publisher.Flux.fromStream;

import com.exigentech.mtgapiclient.model.Card;
import com.exigentech.mtgapiclient.model.Cards;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public final class CardsClientImpl implements CardsClient {

  private final ObjectMapper mapper;
  private final WebClient client;
  private final String url = "https://api.magicthegathering.io/v1/cards";
  private String nextPage = null;
  private String lastPage = null;

  @Autowired
//  public GetCardsClientImpl(WebClient client, @Qualifier("mtgapi.baseurl") URL baseURL) {
  public CardsClientImpl(ObjectMapper mapper, WebClient client) {
    this.mapper = mapper;
    this.client = client;
  }

  @Override
  public Flux<Card> getAll() {
//    final var response = client.get().uri("https://api.magicthegathering.io/v1/cards").exchange();
//    return response.block().bodyToFlux(String.class).flatMap(body -> mapper.readValue(body, new TypeReference<Cards>() {})).doOnError();
//    CardsHeaders headers = null;
//    response.doOnSuccess(r -> new CardsHeaders(r.headers().asHttpHeaders()).next());
    return publishCardPage();
  }

  private Flux<Card> publishCardPage() {
    if (nextPage != null && nextPage == lastPage) {
      nextPage = null;
      lastPage = null;
      return empty();
    }

    return fromStream(() ->
        client.get().uri(
            nextPage == null ? "https://api.magicthegathering.io/v1/cards" : nextPage
        ).exchange().doOnSuccess(n -> {
              final CardsHeaders h = new CardsHeaders(n.headers().asHttpHeaders());
              nextPage = h.next().toString();
              lastPage = h.last().toString();
            }
        ).doOnError(RuntimeException::new).block().bodyToMono(Cards.class).block().cards().stream()
    ).mergeWith(nextPage == lastPage ? empty() : publishCardPage());
  }

  class Derp {

    URI nextPage;
    URI lastPage;
    Set<Card> cards;
  }

//  public ConnectableFlux<Card> derp() {
//
//    Flux.empty().startWith();
//    ConnectableFlux<Cards> flux = ConnectableFlux.create(sink -> {
//          String nextPage = "https://api.magicthegathering.io/v1/cards";
//          String lastPage = null;
//          while (nextPage != lastPage) {
//            sink.next(ImmutableCards.builder().build());
//            client.get().uri(nextPage).exchange().doOnSuccess(r -> {
//              final CardsHeaders h = new CardsHeaders(r.headers().asHttpHeaders());
////              nextPage = h.next().toString();
////              lastPage = h.last().toString();
//            })
//          })

//          return null;
//    return flux;
//    return Flux.fromIterable(responser.cards()).mergeWith(
//      Flux.fromIterable(WebClient.builder().baseUrl("https://api.magicthegathering.io/v1").build().get().uri("/cards").retrieve().bodyToMono(Cards.class).block().cards())
//    );

//    final var response =
////        client.get().uri(url).accept(APPLICATION_JSON).retrieve();
//WebClient.builder().baseUrl("https://api.magicthegathering.io/v1").build().get().uri("/cards").retrieve();
////    WebClient.builder().baseUrl("https://api.magicthegathering.io/v1").build().get().uri("/cards").exchange();
//    return response.bodyToFlux(String.class)
//        .flatMap(body -> {
//          try {
//            final Cards cards = mapper.readValue(body, new TypeReference<Cards>() {});
//            return Flux.fromIterable(cards.cards());
//          } catch (IOException e) {
//            throw new RuntimeException();
//          }
//        }).doOnError(RuntimeException::new);
}
