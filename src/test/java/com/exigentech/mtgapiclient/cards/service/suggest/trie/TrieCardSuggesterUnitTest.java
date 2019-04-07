package com.exigentech.mtgapiclient.cards.service.suggest.trie;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import com.exigentech.mtgapiclient.cards.client.mtgio.model.ImmutableRetrievedCard;
import com.exigentech.mtgapiclient.cards.service.catalog.CardCatalog;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;

@ExtendWith(MockitoExtension.class)
final class TrieCardSuggesterUnitTest {

  @Mock
  private CardCatalog cardCatalog;

  @Test
  void bestMatchWithoutExactPrefix() {
    final var suggester = new TrieCardSuggester();
    final var bestMatch = ImmutableRetrievedCard.builder().name("Arclight Phoenix").build();
    when(cardCatalog.getAllCards()).thenReturn(
        Flux.just(
            ImmutableRetrievedCard.builder().name("Chandra").build(),
            ImmutableRetrievedCard.builder().name("Garruk").build(),
            ImmutableRetrievedCard.builder().name("Tibalt").build(),
            ImmutableRetrievedCard.builder().name("Forked Bolt").build(),
            bestMatch
        )
    );
    suggester.populateTrie(cardCatalog);
    final var suggestions = suggester.suggestCardsByName("rcl");
    assertThat(suggestions.size(), is(5));
    assertThat(suggestions.get(0), is(bestMatch));
  }
}