package com.exigentech.mtgapiclient.search.trie;

import com.exigentech.mtgapiclient.card.catalog.CardCatalog;
import com.exigentech.mtgapiclient.card.catalog.model.Card;
import com.exigentech.mtgapiclient.config.MagicTheGatheringApiConfig;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MagicTheGatheringApiConfig.class)
final class CardSearchTrieIntegrationTest {

  private final CardCatalog cardCatalog;

  @Autowired
  CardSearchTrieIntegrationTest(CardCatalog cardCatalog) {
    this.cardCatalog = cardCatalog;
  }

  @Test
  void buildAndSearch() {
    final Trie<Card> trie = Trie.withKeyMapping(Card::name);
    cardCatalog.getAllCards().take(400)
        .doOnNext(System.out::println)
        .doOnNext(trie::add).blockLast();
//    trie.search("Chand", 4).stream()
//        .map(Card::name)
//        .forEach(System.out::println);
  }

  @Test
  void getMeNames() {
    System.out.println(
        cardCatalog.getAllCards().take(400).map(Card::name).collect(Collectors.toList()).block().stream().collect(Collectors.joining("\\\",\\\""))
    );
  }
}