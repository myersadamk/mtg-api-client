package com.exigentech.mtgapiclient;

import com.exigentech.mtgapiclient.model.Card;
import java.util.Iterator;

public class CrazyIdea implements Iterable<Card> {

  private class CrazyIdeaIterator implements Iterator<Card> {

    @Override
    public boolean hasNext() {
      return false;
    }

    @Override
    public Card next() {
      return null;
    }
  }

  @Override
  public Iterator<Card> iterator() {
    return null;
  }
}
