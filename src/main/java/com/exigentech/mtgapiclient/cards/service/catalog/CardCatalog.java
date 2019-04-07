package com.exigentech.mtgapiclient.cards.service.catalog;

import com.exigentech.mtgapiclient.cards.model.Card;
import reactor.core.publisher.Flux;

public interface CardCatalog {
  Flux<Card> getAllCards();
}
