package com.exigentech.mtgapiclient.cards.service.catalog.model;

import static java.lang.String.format;

import java.util.EnumSet;
import java.util.NoSuchElementException;

public enum Color {
  GREEN("G"), RED("R"), BLUE("U"), BLACK("B"), WHITE("W"), COLORLESS(null);

  private String abbreviation;

  public static Color parse(String representation) {
    return EnumSet.allOf(Color.class).stream()
        .filter(color -> color.name().startsWith(representation) || color.matchesAbbreviation(representation))
        .findFirst()
        .orElseThrow(() -> new NoSuchElementException(format("No Color matching %s found.", representation)));
  }

  private boolean matchesAbbreviation(String representation) {
    switch (this) {
      case COLORLESS:
        return false;
      default:
        return this.abbreviation.equals(representation);
    }
  }

  Color(String abbreviation) {
    this.abbreviation = abbreviation;
  }
}
