package com.exigentech.mtgapiclient.card.catalog;

import com.exigentech.mtgapiclient.card.catalog.model.Color;
import java.util.Optional;
import java.util.Set;
import org.immutables.value.Value.Immutable;

@Immutable
public interface CardCriteria {
  default boolean exclusiveMatch() {
    return true;
  }

  Optional<String> nameContains();
  Optional<Set<Color>> colorIdentity();
}
