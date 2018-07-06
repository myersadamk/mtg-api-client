package com.exigentech.mtgapiclient;

import reactor.core.publisher.Mono;

public interface CardsClient {
  PagedCards getFirstPage();
  PagedCards getNextPage(PagedCards page);
}
