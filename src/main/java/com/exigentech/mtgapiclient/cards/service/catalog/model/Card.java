package com.exigentech.mtgapiclient.cards.service.catalog.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Set;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableCard.class)
public interface Card {
  String name();
  Set<Color> colorIdentity();
  int convertedManaCost();
  // TODO: ton of properties to be added here - oh boy!
  //  Map<Color, Integer> convertedManaCost();
}
