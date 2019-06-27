package com.exigentech.mtgapiclient.cards.client;

import com.exigentech.mtgapiclient.cards.client.model.Page;
import org.springframework.cache.annotation.Cacheable;
import reactor.core.publisher.Mono;

public interface CardsClient {
  @Cacheable(value = "cardPages", cacheManager = "cardPagesCacheManager")
  Mono<Page> getPage(int index);
  Mono<Integer> getLastPageNumber();
}
