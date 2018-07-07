package com.exigentech.mtgapiclient.cards.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableCard.class)
public interface Card {
  String name();
}
