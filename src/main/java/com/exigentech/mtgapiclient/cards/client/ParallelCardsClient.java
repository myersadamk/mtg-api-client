package com.exigentech.mtgapiclient.cards.client;

import com.exigentech.mtgapiclient.cards.client.model.Page;
import reactor.core.publisher.Mono;

public interface ParallelCardsClient {
  Mono<Integer> getPageCount();
  // wonder if I should encapsulate the 1-based indexing in favor of 0-based
  // TODO: cache
  Mono<Page> getPage(int index);
}
