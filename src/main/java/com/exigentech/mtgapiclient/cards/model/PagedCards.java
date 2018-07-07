package com.exigentech.mtgapiclient.cards.model;

import java.net.URI;
import java.util.Set;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;

@Immutable
@Style(depluralize = true)
public interface PagedCards {
  URI self();
  URI next();
  URI last();
  Set<Card> cards();
}
