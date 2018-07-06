package com.exigentech.mtgapiclient;

import reactor.core.publisher.Mono;

public interface CardsClient {
  Mono<? extends PagedCards> getFirstPage();
  Mono<? extends PagedCards> getNextPage(PagedCards page);
}
