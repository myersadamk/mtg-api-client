package com.exigentech.mtgapiclient.config;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import com.exigentech.mtgapiclient.cards.client.CardsClient;
import com.exigentech.mtgapiclient.cards.client.model.RawCard;
import com.exigentech.mtgapiclient.cards.client.mtgio.MagicTheGatheringCardsClient;
import com.exigentech.mtgapiclient.cards.client.util.BodyParser;
import com.exigentech.mtgapiclient.cards.service.catalog.CardCatalog;
import com.exigentech.mtgapiclient.cards.service.catalog.mapping.RawCardToCardMapper;
import com.exigentech.mtgapiclient.cards.service.catalog.model.Card;
import com.exigentech.mtgapiclient.cards.service.catalog.mtgio.MagicTheGatheringCardCatalog;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableCaching
public class MagicTheGatheringApiConfig {

  @Bean
  public WebClient webClient() {
    return WebClient.builder().build();
  }

  @Bean
  @Scope("prototype")
  public ObjectMapper objectMapper() {
    return new ObjectMapper()
        .configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
        .findAndRegisterModules();
  }

  @Bean
  public BodyParser jsonDeserializer(ObjectMapper objectMapper) {
    return new BodyParser(objectMapper);
  }

  @Bean(name = "cardPagesCacheManager")
  public CacheManager cardPagesCacheManager() {
    return new ConcurrentMapCacheManager("cardPages");
  }

  @Bean
  public Converter<RawCard, Card> rawCardToCardMapper() {
    return new RawCardToCardMapper();
  }

  @Bean
  public CardsClient cardsClient(@Value("${api.mtgio.cards.uri}") String baseUri, BodyParser bodyParser, WebClient webClient) {
    return new MagicTheGatheringCardsClient(baseUri, webClient, bodyParser);
  }

  @Bean
  public CardCatalog cardCatalog(CardsClient cardsClient, Converter<RawCard, Card> mapper) {
    return new MagicTheGatheringCardCatalog(cardsClient, mapper);
  }
}
