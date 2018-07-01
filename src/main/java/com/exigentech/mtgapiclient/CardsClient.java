package com.exigentech.mtgapiclient;

import com.exigentech.mtgapiclient.model.Card;
import reactor.core.publisher.Flux;

public interface CardsClient {
  Flux<Card> getAll();
}
