package com.exigentech.mtgapiclient.card.client.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Optional;
import java.util.Set;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableRawCard.class)
public interface RawCard {
  String name();
  String rarity();
  double cmc();
  Optional<String> text();
  Optional<String> manaCost();
  Set<String> colorIdentity();
  Set<String> colors();
  Set<String> types();
}