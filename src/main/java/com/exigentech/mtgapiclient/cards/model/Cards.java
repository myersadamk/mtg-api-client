package com.exigentech.mtgapiclient.cards.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Set;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableCards.class)
public interface Cards {
  Set<Card> cards();
}
