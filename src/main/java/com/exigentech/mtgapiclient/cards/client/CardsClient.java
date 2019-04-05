package com.exigentech.mtgapiclient.cards.client;

import com.exigentech.mtgapiclient.cards.model.CardPage;
import reactor.core.publisher.Mono;

public interface CardsClient {
  Mono<CardPage> getFirstPage();
  Mono<CardPage> getNextPage(CardPage page);
}
