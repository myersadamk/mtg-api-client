package com.exigentech.mtgapiclient.service.suggest.trie;

import static java.util.stream.Collectors.toList;

import com.exigentech.mtgapiclient.card.catalog.model.Card;
import com.exigentech.mtgapiclient.card.catalog.CardCatalog;
import com.exigentech.mtgapiclient.service.suggest.CardSuggester;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class TrieCardSuggester implements CardSuggester {

  private final Trie<String, Card> trie;

  public TrieCardSuggester() {
    trie = new PatriciaTrie<>();
  }

  @PostConstruct
  public void populateTrie(@Autowired CardCatalog cardCatalog) {
    final var allCards = cardCatalog.getAllCards();
    allCards.subscribe((card) -> trie.put(card.name(), card));
    allCards.blockLast();
  }

  @Override
  public List<Card> suggestCardsByName(String name) {
    final var matches = new ArrayList<>(trie.prefixMap(name).values());

    final var exactMatchCount = matches.size();
    if (exactMatchCount >= 10) {
      return matches.stream().limit(10).collect(toList());
    }

    matches.addAll(trie.headMap(name).values().stream().limit(10 - exactMatchCount).collect(toList()));
    final var fuzzyMatchCount = matches.size();
    if (fuzzyMatchCount == 10) {
      return matches;
    }

    matches.addAll(trie.tailMap(name).values().stream().limit(10 - fuzzyMatchCount).collect(toList()));

    return matches;
  }
}
