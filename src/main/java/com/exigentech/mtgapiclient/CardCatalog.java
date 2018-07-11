package com.exigentech.mtgapiclient;

import com.exigentech.mtgapiclient.cards.CardsClient;
import com.exigentech.mtgapiclient.cards.model.Card;
import com.exigentech.mtgapiclient.cards.model.PagedCards;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class CardCatalog {

  private final CardsClient client;

  @Autowired
  public CardCatalog(CardsClient client) {
    this.client = client;
  }

  // Using relationships
  public Flux<Card> getAllCards() {
    final Flux<PagedCards> pages = Flux.generate(client::getFirstPage, (response, sink) -> {
      final var page = response.block();
      sink.next(page);
      if (page.self().equals(page.last())) {
        sink.complete();
        return response;
      }
      return client.getNextPage(page);
    });
    return pages.flatMapIterable(PagedCards::cards);
  }
}
