package com.exigentech.mtgapiclient.search.trie;

import java.util.function.Function;
import org.junit.jupiter.api.Test;

class RedundantTrieTest {

//  @Test
//  void get_requiresExactMatch() {
//    final var trie = Trie.forIterableType();
//    trie.add(List.of(1, 2), List.of(2, 3));
//    assertThat(trie.get("a")).isEmpty();
//    assertThat(trie.get("ab")).isEqualTo(Optional.of("ab"));
//    assertThat(trie.get("abc")).isEqualTo(Optional.empty());
//  }

  @Test
  void get_requiresExactMatch() {
    final Trie<String> trie = Trie.withKeyMapping(Function.identity());
    trie.add("abcd");
//    trie.search("ab", 2);
    trie.search("bbcd", 2);
//    assertThat(trie.get("a")).isEmpty();
//    assertThat(trie.get("abc")).isEmpty();
//    assertThat(trie.get("ab")).isEqualTo(Optional.of("ab"));
  }

  @Test
  void get_multipleBranches_requiresExactMatch() {
    final Trie<String> trie = Trie.withKeyMapping(Function.identity());
    trie.add("abc");
    trie.add("ax");
//    assertThat(trie.get("abc")).isEqualTo(Optional.of("abc"));
//    assertThat(trie.get("ax")).isEqualTo(Optional.of("ax"));
//    assertThat(trie.get("a")).isEmpty();
//    assertThat(trie.get("x")).isEmpty();
  }
}