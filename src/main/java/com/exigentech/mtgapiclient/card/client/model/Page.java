package com.exigentech.mtgapiclient.card.client.model;

import java.util.List;
import java.util.Optional;
import org.immutables.value.Value.Immutable;

@Immutable
public interface Page {
  List<RawCard> cards();
  Optional<Integer> nextPageNumber();
  Integer lastPageNumber();
}
