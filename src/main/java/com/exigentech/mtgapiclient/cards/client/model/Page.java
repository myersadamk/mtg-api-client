package com.exigentech.mtgapiclient.cards.client.model;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import org.immutables.value.Value.Immutable;

@Immutable
public interface Page {
  URI self();
  URI last();
  Optional<URI> next();
  List<RawCard> cards();
}
