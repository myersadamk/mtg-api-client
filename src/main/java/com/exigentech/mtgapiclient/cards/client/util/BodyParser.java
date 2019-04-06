package com.exigentech.mtgapiclient.cards.client.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.function.Function;

/**
 * Class may not be carrying its weight.. essentially this just wraps ObjectMapper so we don't have
 * to deal with an annoying try/catch with an IO exception.
 */
public class BodyParser {

  private final ObjectMapper mapper;

  public BodyParser(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  public <T> T parse(Class<T> clazz, String body) {
    return parse(clazz, body, RuntimeException::new);
  }

  public <T, R extends RuntimeException>
  T parse(Class<T> clazz, String body, Function<IOException, R> handler) {
    try {
      return mapper.readValue(body, clazz);
    } catch (IOException exception) {
      throw handler.apply(exception);
    }
  }
}
