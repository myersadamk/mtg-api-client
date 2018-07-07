package com.exigentech.mtgapiclient;

import static java.util.function.Function.identity;
import static reactor.core.publisher.Flux.generate;

import com.exigentech.mtgapiclient.cards.CardsClient;
import com.exigentech.mtgapiclient.cards.model.Card;
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

  public Flux<Card> getAllCards() {
    return Flux.create((sink) -> {
      final var page = client.getFirstPage();

      page.flux().flatMapIterable(p -> p.cards()).subscribe(p -> sink.next(p));

      page.flux().flatMapIterable(p -> p.cards()).subscribe(p -> sink.next(p));

    });
//    return Flux.generate(client::getFirstPage, (page, sink) -> {
//      final Mono<? extends PagedCards> nextPage = client.getNextPage(page.block());
//      sink.complete();
//      return page;
//    });
  }
//    final Flux<Collection<Card>> pagedFlux = generate(client::getFirstPage, (page, sink) -> {
//          sink.next(page.cards());
//          if (page.self().equals(page.last())) {
//            sink.complete();
//            return null;
//          }
//          return client.getNextPage(page);
//        }
//    );
//    return pagedFlux.flatMapIterable(identity());
}
