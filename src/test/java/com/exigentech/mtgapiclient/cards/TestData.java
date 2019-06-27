package com.exigentech.mtgapiclient.cards;

import com.exigentech.mtgapiclient.cards.client.model.ImmutableRawCard;
import com.exigentech.mtgapiclient.cards.client.model.RawCard;
import com.exigentech.mtgapiclient.cards.service.catalog.mapping.RawCardToCardMapper;
import com.exigentech.mtgapiclient.cards.service.catalog.model.Card;
import com.google.common.collect.ImmutableSet;

// TODO: there's a better way to organize and access this stuff.
public interface TestData {
  RawCard RAW_FLAMEWAVE_INVOKER =
      ImmutableRawCard.builder()
          .name("Flamewave Invoker")
          .rarity("Uncommon")
          .cmc(3.0)
          .text("{7}{R}: Flamewave Invoker deals 5 damage to target player or planeswalker.")
          .manaCost("{2}{R}")
          .colorIdentity(ImmutableSet.of("R"))
          .colors(ImmutableSet.of("Red"))
          .types(ImmutableSet.of("Creature"))
          .build();

  RawCard RAW_CANOPY_SPIDER =
      ImmutableRawCard.builder()
          .name("Canopy Spider")
          .rarity("Common")
          .cmc(2.0)
          .text("Reach (This creature can block creatures with flying.)")
          .manaCost("{1}{G}")
          .colorIdentity(ImmutableSet.of("G"))
          .colors(ImmutableSet.of("Green"))
          .types(ImmutableSet.of("Creature"))
          .build();

  RawCardToCardMapper MAPPER = new RawCardToCardMapper();

  Card MAPPED_FLAMEWAVE_INVOKER = MAPPER.convert(RAW_FLAMEWAVE_INVOKER);
  Card MAPPED_CANOPY_SPIDER = MAPPER.convert(RAW_CANOPY_SPIDER);
}
