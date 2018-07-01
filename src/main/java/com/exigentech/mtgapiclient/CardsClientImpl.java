package com.exigentech.mtgapiclient;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static reactor.core.publisher.Flux.fromIterable;

import com.exigentech.mtgapiclient.model.Card;
import com.exigentech.mtgapiclient.model.Cards;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Component
public final class CardsClientImpl implements CardsClient {

  private final ObjectMapper mapper;
  private final WebClient client;
  private final String url = "https://api.magicthegathering.io/v1/cards";

  @Autowired
//  public GetCardsClientImpl(WebClient client, @Qualifier("mtgapi.baseurl") URL baseURL) {
  public CardsClientImpl(ObjectMapper mapper, WebClient client) {
    this.mapper = mapper;
    this.client = client;
  }

  @Override
  public Flux<Card> getAll() {
    final var responser =
//        client.get().uri(url).accept(APPLICATION_JSON).retrieve();
        WebClient.builder().baseUrl("https://api.magicthegathering.io/v1").build().get().uri("/cards").retrieve().bodyToMono(Cards.class).block();

    return Flux.fromIterable(responser.cards()).mergeWith(
      Flux.fromIterable(WebClient.builder().baseUrl("https://api.magicthegathering.io/v1").build().get().uri("/cards").retrieve().bodyToMono(Cards.class).block().cards())
    );
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
}

