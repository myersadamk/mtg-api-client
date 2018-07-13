package com.exigentech.mtgapiclient.cards.client.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.function.Function;

public interface Model {

  static <T extends Model, R extends RuntimeException>
  T deserialize(String body, ObjectMapper mapper, final Function<IOException, R> handler) {
    try {
      final T jsonProperty = mapper.readValue(body, new TypeReference<T>(){});
      return jsonProperty;
    } catch (IOException exception) {
      throw handler.apply(exception);
    }
  }

  private static <T extends Model> TypeReference<T> type() {
    return new TypeReference<>() {
    };
  }
}
