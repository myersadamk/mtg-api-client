package com.exigentech.mtgapiclient.cards.service.catalog.mtgio;

import static reactor.core.publisher.Flux.concat;
import static reactor.core.publisher.Flux.empty;
import static reactor.core.publisher.Flux.just;
import static reactor.core.publisher.Flux.range;

import com.exigentech.mtgapiclient.cards.client.CardsClient;
import com.exigentech.mtgapiclient.cards.client.model.Page;
import com.exigentech.mtgapiclient.cards.client.model.RawCard;
import com.exigentech.mtgapiclient.cards.service.catalog.CardCatalog;
import com.exigentech.mtgapiclient.cards.service.catalog.CardCriteria;
import com.exigentech.mtgapiclient.cards.service.catalog.model.Card;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
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

  @Override
  public Flux<Card> getAllCards() {
    return client.getPage(1)
        .flatMapMany(page -> {

      final var remainingPages = page.nextPageNumber().map(nextPageNumber -> {
        final var lastPageNumber = page.lastPageNumber();

        if (nextPageNumber.equals(lastPageNumber)) {
          return client.getPage(lastPageNumber).flux();
        }

        return concat(range(nextPageNumber, lastPageNumber).map(client::getPage));
      }).orElse(empty());

      return Flux.merge(just(page), remainingPages)
          .map(Page::cards)
          .flatMap(Flux::fromIterable)
          .map(mapper::convert);
    });
  }

  @Override
  public Flux<Card> matchCards(CardCriteria criteria) {
    final var matcher = new CardMatcher(criteria);
    return getAllCards().filter(matcher::test);
  }

  private static final class CardMatcher implements Predicate<Card> {

    private final CardCriteria criteria;

    private CardMatcher(CardCriteria criteria) {
      this.criteria = criteria;
    }

    @Override
    public boolean test(Card card) {
      final var conditions = Stream.of(
          checkCondition(criteria::nameContains, (name) -> card.name().contains(name)),
          checkCondition(criteria::colorIdentity, (color) -> card.colorIdentity().containsAll(color))
      );

      return criteria.exclusiveMatch() ? conditions.allMatch(p -> p.test(card)) : conditions.anyMatch(p -> p.test(card));
    }

    private static <T> Predicate<Card> checkCondition(Supplier<Optional<T>> criteria, Predicate<T> condition) {
      return (c) -> criteria.get().map(condition::test).orElse(true);
    }
  }
}
