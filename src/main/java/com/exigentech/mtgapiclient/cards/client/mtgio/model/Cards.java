package com.exigentech.mtgapiclient.cards.client.mtgio.model;

import com.exigentech.mtgapiclient.cards.model.Card;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.Set;
import org.immutables.value.Value;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Parameter;
import org.immutables.value.Value.Style;
import org.immutables.value.Value.Style.BuilderVisibility;
import org.immutables.value.internal.$processor$.meta.$BuilderMirrors.Factory;

@Immutable(builder = false)
@JsonDeserialize(as = ImmutableCards.class)
public interface Cards {
  @Parameter
  Set<Card> cards();
}
