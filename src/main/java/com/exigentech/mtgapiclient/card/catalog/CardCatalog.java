package com.exigentech.mtgapiclient.card.catalog;

import com.exigentech.mtgapiclient.card.catalog.model.Card;
import reactor.core.publisher.Flux;

public interface CardCatalog {
  Flux<Card> getAllCards();
  Flux<Card> matchCards(CardCriteria criteria);
}
