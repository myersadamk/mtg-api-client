package com.exigentech.mtgapiclient.cards.client;

import com.exigentech.mtgapiclient.cards.client.model.Page;
import reactor.core.publisher.Mono;

public interface CardsClient {
  Mono<Page> getFirstPage();
  Mono<Page> getNextPage(Page page);
  Mono<Page> getLastPage(Page page);
}
