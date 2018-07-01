package com.exigentech.mtgapiclient.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableCards.class)
public interface Cards {
  List<Card> cards();
}
