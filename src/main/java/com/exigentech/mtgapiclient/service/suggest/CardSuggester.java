package com.exigentech.mtgapiclient.service.suggest;

import com.exigentech.mtgapiclient.card.catalog.model.Card;
import java.util.Collection;

public interface CardSuggester {
  Collection<Card> suggestCardsByName(String name);
}
