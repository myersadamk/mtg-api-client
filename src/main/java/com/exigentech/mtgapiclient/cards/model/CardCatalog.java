package com.exigentech.mtgapiclient.cards.model;

import static reactor.core.publisher.Flux.generate;

import com.exigentech.mtgapiclient.cards.client.CardPage;
import com.exigentech.mtgapiclient.cards.client.CardsClient;
import com.exigentech.mtgapiclient.cards.model.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public final class CardCatalog {

  private final CardsClient client;

  @Autowired
  public CardCatalog(CardsClient client) {
    this.client = client;
  }

  // Using relationships
  public Flux<Card> getAllCards() {
    final var pageGenerator = Flux.<CardPage, Mono<? extends CardPage>>
        generate(
        client::getFirstPage, (response, sink) -> {
          final var page = response.block();
          sink.next(page);

          if (page.next().isPresent()) {
            return client.getNextPage(page);
          }
          sink.complete();
          return null;
        });
    return pageGenerator.flatMapIterable(CardPage::cards);
  }
}
