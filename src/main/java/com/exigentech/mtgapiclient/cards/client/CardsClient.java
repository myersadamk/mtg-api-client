package com.exigentech.mtgapiclient.cards.client;

import reactor.core.publisher.Mono;

public interface CardsClient {
  Mono<? extends CardPage> getFirstPage();
  Mono<? extends CardPage> getNextPage(CardPage page);
}
