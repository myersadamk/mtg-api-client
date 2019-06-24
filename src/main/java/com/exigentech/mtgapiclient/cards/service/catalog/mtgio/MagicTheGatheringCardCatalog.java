package com.exigentech.mtgapiclient.cards.service.catalog.mtgio;

import static reactor.core.publisher.Flux.generate;

import com.exigentech.mtgapiclient.cards.client.CardsClient;
import com.exigentech.mtgapiclient.cards.client.model.Page;
import com.exigentech.mtgapiclient.cards.client.model.RawCard;
import com.exigentech.mtgapiclient.cards.service.catalog.CardCatalog;
import com.exigentech.mtgapiclient.cards.service.catalog.CardCriteria;
import com.exigentech.mtgapiclient.cards.service.catalog.model.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public final class MagicTheGatheringCardCatalog implements CardCatalog {

  private final CardsClient client;
  private final Converter<RawCard, Card> mapper;

  @Autowired
  public MagicTheGatheringCardCatalog(CardsClient client, Converter<RawCard, Card> mapper) {
    this.client = client;
    this.mapper = mapper;
  }

  public Flux<Card> getAllCards() {
    final Flux<Page> pageFlux =
        generate(client::getFirstPage, (response, sink) -> {
          final var page = response.block();
          sink.next(page);

          if (page.next().isPresent()) {
            return client.getNextPage(page);
          }
          sink.complete();
          return null;
        });

    return pageFlux.flatMapIterable(Page::cards).map(mapper::convert);
  }

  @Override
  public Flux<Card> matchCards(CardCriteria criteria) {
    return getAllCards().filter(card -> {
      boolean matchesCriteria = true;

      matchesCriteria &= criteria.nameContains().map(substring -> card.name().contains(substring))
          .orElse(true);

      matchesCriteria &= criteria.colorIdentity().map(colorIdentity -> card.colorIdentity().containsAll(colorIdentity))
          .orElse(true);

      return matchesCriteria;
    });
  }
}
