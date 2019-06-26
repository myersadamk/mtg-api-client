package com.exigentech.mtgapiclient.cards.client.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.function.Function;
import org.springframework.stereotype.Component;

@Component
public class BodyParser {

  private final ObjectMapper mapper;

  public BodyParser(ObjectMapper mapper) {
    this.mapper = mapper;
  }

  public <T> T parse(Class<T> clazz, String body) {
    return parse(clazz, body, (thrown) -> new BodyParsingException(thrown));
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
