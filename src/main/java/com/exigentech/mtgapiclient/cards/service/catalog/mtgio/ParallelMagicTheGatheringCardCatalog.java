package com.exigentech.mtgapiclient.cards.service.catalog.mtgio;

import static reactor.core.publisher.Flux.concat;
import static reactor.core.publisher.Flux.range;

import com.exigentech.mtgapiclient.cards.client.ParallelCardsClient;
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
public final class ParallelMagicTheGatheringCardCatalog implements CardCatalog {

  private final ParallelCardsClient client;
  private final Converter<RawCard, Card> mapper;

  @Autowired
  public ParallelMagicTheGatheringCardCatalog(ParallelCardsClient client, Converter<RawCard, Card> mapper) {
    this.client = client;
    this.mapper = mapper;
  }

  @Override
  public Flux<Card> getAllCards() {
    return client.getPage(1).flatMapMany(response -> {
        Flux<Page> pages = concat(
            range(
                response.currentPageNumber() + 1, response.finalPageNumber()
            ).map(client::getPage)
        );

        return Flux.merge(Flux.just(response), pages).map(Page::cards).flatMap(Flux::fromIterable).map(mapper::convert);
    });
  }

  @Override
  public Flux<Card> matchCards(CardCriteria criteria) {
    final var matcher = new CardMatcher(criteria);
    return getAllCards().filter(matcher::test);
  }

  private static final class CardMatcher implements Predicate<Card> {
    private final CardCriteria criteria;
    private final boolean isExclusiveMatchRequired;

    private CardMatcher(CardCriteria criteria) {
      this.criteria = criteria;
      isExclusiveMatchRequired = criteria.exclusiveMatch();
    }

    @Override
    public boolean test(Card card) {
      boolean matchesCriteria = false;

      final Stream<Predicate<Card>> conditions = Stream.of(
          checkCondition(criteria::nameContains, card::name, (name) -> card.name().contains(name)),
          checkCondition(criteria::colorIdentity, card::colorIdentity, (color) -> card.colorIdentity().containsAll(color))
      );

      return criteria.exclusiveMatch() ? conditions.allMatch(p -> p.test(card)) : conditions.anyMatch(p -> p.test(card));
    }

    private static <T> Predicate<Card> checkCondition(Supplier<Optional<T>> criteria, Supplier<T> cardField, Predicate<T> condition) {
      return (c) -> criteria.get().map(condition::test).orElse(true);
    }
  }

//  private static <T> boolean checkCondition(Supplier<Optional<?>> criteria, Supplier<T> property, Predicate<T> condition) {
}
