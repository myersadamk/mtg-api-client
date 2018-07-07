package com.exigentech.mtgapiclient.cards;

import com.exigentech.mtgapiclient.cards.model.PagedCards;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface CardsClient {
  Mono<? extends PagedCards> getFirstPage();
  Mono<? extends PagedCards> getNextPage(PagedCards page);
}
