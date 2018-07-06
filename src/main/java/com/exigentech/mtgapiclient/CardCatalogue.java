package com.exigentech.mtgapiclient;

import static java.util.function.Function.identity;
import static reactor.core.publisher.Flux.generate;

import com.exigentech.mtgapiclient.model.Card;
import java.util.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink.OverflowStrategy;
import reactor.core.publisher.Mono;

public class CardCatalogue {

  private final CardsClient client;

  @Autowired
  public CardCatalogue(CardsClient client) {
    this.client = client;
  }

  Flux<? extends PagedCards> getAllCards() {
    final var lol = client.getFirstPage();
    Flux.create(sink -> {

    }, OverflowStrategy.BUFFER).subscribe();

    return null;
//      generate(firstPage, (state, sink) -> {
//          if (page.last().equals(page.next())) {
//
//          }
//          sink.next(page);
//          return client.getNextPage(page);
//        })
//    );
  }
}
