package com.exigentech.mtgapiclient;

import com.exigentech.mtgapiclient.model.Card;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.net.URI;
import java.util.Collection;
import org.immutables.value.Value.Immutable;
import org.immutables.value.Value.Style;
import org.springframework.web.reactive.function.client.WebClient.ResponseSpec;

@Immutable
@Style(depluralize = true)
interface PagedCards {
  URI self();
  URI next();
  URI last();
  Collection<Card> cards();

  static PagedCards fromCall(ResponseSpec response, ObjectMapper mapper) {
    return null;
  }
}
