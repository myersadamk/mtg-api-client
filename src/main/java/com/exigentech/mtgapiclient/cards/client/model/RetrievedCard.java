package com.exigentech.mtgapiclient.cards.client.model;

import com.exigentech.mtgapiclient.cards.model.Card;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value.Immutable;

@Immutable
@JsonDeserialize(as = ImmutableRetrievedCard.class)
public interface RetrievedCard extends Card {
}
