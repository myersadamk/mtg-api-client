package com.exigentech.mtgapiclient.cards.client.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;

@Immutable(builder = false)
@JsonDeserialize(as = ImmutableRawCards.class)
public interface RawCards {
  @Parameter
  List<RawCard> cards();
}
