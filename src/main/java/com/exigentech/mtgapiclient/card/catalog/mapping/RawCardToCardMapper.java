package com.exigentech.mtgapiclient.card.catalog.mapping;

import com.exigentech.mtgapiclient.card.client.model.RawCard;
import com.exigentech.mtgapiclient.card.catalog.model.Card;
import com.exigentech.mtgapiclient.card.catalog.model.Color;
import com.exigentech.mtgapiclient.card.catalog.model.ImmutableCard;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public final class RawCardToCardMapper implements Converter<RawCard, Card> {

  @Override
  public Card convert(RawCard rawCard) {
    return ImmutableCard.builder()
        .name(rawCard.name())
        .colorIdentity(rawCard.colorIdentity().stream().map(Color::parse).collect(Collectors.toSet()))
        .convertedManaCost(Double.valueOf(rawCard.cmc()).intValue())
//        .convertedManaCost(CONVERTED_MANA_COST_MAPPER::convert)
        .build();
  }

  private static final Converter<Set<String>, Map<Color, Integer>> CONVERTED_MANA_COST_MAPPER = (cmc) -> {
    return null;
  };

}
