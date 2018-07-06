package com.exigentech.mtgapiclient;

import static reactor.core.publisher.Flux.generate;

import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;

public class CardCatalogue {

  private final CardsClient client;

  @Autowired
  public CardCatalogue(CardsClient client) {
    this.client = client;
  }

  Flux<? extends PagedCards> getAllCards() {
    return generate(client::getFirstPage, (page, sink) -> {
      sink.next(page);

      if (page.self().equals(page.last())) {
        sink.complete();
        return null;
      }

      return client.getNextPage(page);
    });
  }
}
