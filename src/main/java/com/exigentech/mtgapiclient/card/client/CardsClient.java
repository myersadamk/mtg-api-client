package com.exigentech.mtgapiclient.card.client;

import com.exigentech.mtgapiclient.card.client.model.Page;
import org.springframework.cache.annotation.Cacheable;
import reactor.core.publisher.Mono;

public interface CardsClient {
  @Cacheable(value = "cardPages", cacheManager = "cardPagesCacheManager")
  Mono<Page> getPage(int index);
  Mono<Integer> getLastPageNumber();
}
