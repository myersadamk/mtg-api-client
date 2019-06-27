package com.exigentech.mtgapiclient.cards.client.mtgio;

import static java.lang.String.format;
import static java.util.regex.Pattern.compile;
import static org.springframework.http.HttpHeaders.LINK;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.http.HttpHeaders;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

// TODO: alllll of this header/URI related stuff, both here and in the client, should be made into a component that can be unit tested apart
// from all of the network I/O stuff.
final class PageHeaderIntrospector {

  private static final String LINK_RELATIONSHIP_PATTERN = "<([\\w:/\\.\\d\\?=]+)>; rel=\"%s\"";
  private static final Pattern NEXT = compile(format(LINK_RELATIONSHIP_PATTERN, "next"));
  private static final Pattern LAST = compile(format(LINK_RELATIONSHIP_PATTERN, "last"));

  static Optional<UriComponents> getLastPageUri(final HttpHeaders headers) {
    return getLink(headers, LAST);
  }

  static Optional<UriComponents> getNextPageUri(final HttpHeaders headers) {
    return getLink(headers, NEXT);
  }

  private static Optional<UriComponents> getLink(final HttpHeaders headers, final Pattern pattern) {
    return headers.get(LINK).stream().map(content -> {
      final Matcher matcher = pattern.matcher(content);
      if (matcher.find() && matcher.groupCount() >= 1) {
        return matcher.group(1);
      }
      return null;
    }).filter(group -> group != null).findFirst().map(uri -> UriComponentsBuilder.fromUriString(uri).build());
  }

  private PageHeaderIntrospector() {
  }
}
