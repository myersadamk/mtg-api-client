package com.exigentech.mtgapiclient;

import static org.springframework.http.HttpHeaders.*;

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.http.HttpHeaders;

public final class CardsHeaders {

  private final HttpHeaders headers;

  private enum LinkRel {
    last, next;
  }

  public CardsHeaders(HttpHeaders headers) {
    this.headers = headers;
  }

  public URI next() {
    return getLinkByRelation(LinkRel.next);
  }

  public URI last() {
    return getLinkByRelation(LinkRel.last);
  }

  private URI getLinkByRelation(LinkRel rel) {
    final Pattern pattern = Pattern.compile("<([\\w:/\\.\\d\\?=]+)>; rel=\"" + rel.name() + "\"");
    return URI.create(
        headers.get(LINK).stream().map(content -> {
          final Matcher matcher = pattern.matcher(content);
          if (matcher.find() && matcher.groupCount() >= 1) {
            return matcher.group(1);
          }
          return null;
        }).filter(group -> group != null).findFirst().orElseThrow(RuntimeException::new)
    );
  }
//      [<https://api.magicthegathering.io/v1/cards?page=363>; rel="last", <https://api.magicthegathering.io/v1/cards?page=2>; rel="next"]
}
