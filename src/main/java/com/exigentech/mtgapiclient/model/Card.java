package com.exigentech.mtgapiclient.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableCard.class)
public interface Card {
  String name();
}
