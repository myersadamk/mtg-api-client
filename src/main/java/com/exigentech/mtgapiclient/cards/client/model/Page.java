package com.exigentech.mtgapiclient.cards.client.model;

import java.util.List;
import java.util.Optional;
import org.immutables.value.Value.Immutable;

@Immutable
public interface Page {
  Optional<Integer> nextPageNumber();
  Integer lastPageNumber();
  List<RawCard> cards();
}
