package com.exigentech.mtgapiclient.cards.client.model;

import java.net.URI;
import java.util.Optional;
import java.util.Set;
import org.immutables.value.Value.Immutable;

@Immutable
public interface Page {
  URI self();
  URI last();
  Optional<URI> next();
  Set<RawCard> cards();
}
