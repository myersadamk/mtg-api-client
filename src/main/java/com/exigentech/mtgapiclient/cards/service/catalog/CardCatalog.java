package com.exigentech.mtgapiclient.cards.service.catalog;

import com.exigentech.mtgapiclient.cards.service.catalog.model.Card;
import reactor.core.publisher.Flux;

public interface CardCatalog {
  Flux<Card> getAllCards();
  Flux<Card> matchCards(CardCriteria criteria);
}
