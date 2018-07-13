package com.exigentech.mtgapiclient.cards.client;

import com.exigentech.mtgapiclient.Card;
import java.net.URI;
import java.util.Optional;
import java.util.Set;
import org.immutables.value.Value.Immutable;

@Immutable
public interface CardPage {
  URI self();
  URI last();
  Optional<URI> next();
  Set<Card> cards();
}
