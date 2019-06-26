package com.exigentech.mtgapiclient.cards.client;

public class CardsClientException extends RuntimeException {

  public CardsClientException(String message, Throwable cause) {
    super(message, cause);
  }

  public CardsClientException(String message) {
    super(message);
  }
}
