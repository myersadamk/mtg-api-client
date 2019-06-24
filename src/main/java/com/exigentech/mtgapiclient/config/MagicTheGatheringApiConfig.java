package com.exigentech.mtgapiclient.config;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;

import com.exigentech.mtgapiclient.cards.client.CardsClient;
import com.exigentech.mtgapiclient.cards.client.mtgio.MagicTheGatheringCardsClient;
import com.exigentech.mtgapiclient.cards.client.util.BodyParser;
import com.exigentech.mtgapiclient.cards.service.catalog.CardCatalog;
import com.exigentech.mtgapiclient.cards.service.catalog.mapping.RawCardToCardMapper;
import com.exigentech.mtgapiclient.cards.service.catalog.mtgio.MagicTheGatheringCardCatalog;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
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

  @Bean
  public RawCardToCardMapper rawCardToCardMapper() {
    return new RawCardToCardMapper();
  }

  @Bean
  public CardsClient cardsClient(BodyParser bodyParser, WebClient webClient, RawCardToCardMapper mapper) {
    return new MagicTheGatheringCardsClient(webClient, bodyParser, mapper);
  }

  @Bean
  public CardCatalog cardCatalog(CardsClient cardsClient, RawCardToCardMapper mapper) {
    return new MagicTheGatheringCardCatalog(cardsClient, mapper);
  }
}
