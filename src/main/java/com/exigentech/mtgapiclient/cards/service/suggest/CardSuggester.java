package com.exigentech.mtgapiclient.cards.service.suggest;

import com.exigentech.mtgapiclient.cards.model.Card;
import java.util.Collection;

public interface CardSuggester {
  Collection<Card> suggestCardsByName(String name);
}
