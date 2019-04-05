package com.exigentech.mtgapiclient.cards.service.mtgio;

import static reactor.core.publisher.Flux.generate;

import com.exigentech.mtgapiclient.cards.model.CardPage;
import com.exigentech.mtgapiclient.cards.client.CardsClient;
import com.exigentech.mtgapiclient.cards.model.Card;
import com.exigentech.mtgapiclient.cards.service.CardCatalog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public final class MagicTheGatheringCardCatalog implements CardCatalog {

  private final CardsClient client;

  @Autowired
  public MagicTheGatheringCardCatalog(CardsClient client) {
    this.client = client;
  }

  public Flux<Card> getAllCards() {
    final Flux<CardPage> pageFlux =
        generate(client::getFirstPage, (response, sink) -> {
          final var page = response.block();
          sink.next(page);

          if (page.next().isPresent()) {
            return client.getNextPage(page);
          }
          sink.complete();
          return null;
        });

    return pageFlux.flatMapIterable(CardPage::cards);
  }
}
