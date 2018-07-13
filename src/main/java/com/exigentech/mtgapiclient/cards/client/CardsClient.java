package com.exigentech.mtgapiclient.cards.client;

import reactor.core.publisher.Mono;

public interface CardsClient {
  Mono<CardPage> getFirstPage();
  Mono<CardPage> getNextPage(CardPage page);
}
