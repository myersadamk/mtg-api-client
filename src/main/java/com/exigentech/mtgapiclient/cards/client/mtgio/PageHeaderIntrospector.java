package com.exigentech.mtgapiclient.cards.client.mtgio;

import static java.util.regex.Pattern.compile;
import static org.springframework.http.HttpHeaders.LINK;

import com.exigentech.mtgapiclient.cards.client.model.ImmutablePage.Builder;
import java.net.URI;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.http.HttpHeaders;

final class PageHeaderIntrospector {

  private static final String LINK_RELATIONSHIP_PATTERN = "<([\\w:/\\.\\d\\?=]+)>; rel=\"%s\"";
  private static final Pattern NEXT = compile(String.format(LINK_RELATIONSHIP_PATTERN, "next"));
  private static final Pattern LAST = compile(String.format(LINK_RELATIONSHIP_PATTERN, "last"));

  static void populateLinks(final URI self, final HttpHeaders headers, final Builder builder) {
    builder.next(getLink(headers, NEXT));
    builder.last(getLink(headers, LAST).orElse(self));
  }

  private static Optional<URI> getLink(final HttpHeaders headers, final Pattern pattern) {
    return headers.get(LINK).stream().map(content -> {
      final Matcher matcher = pattern.matcher(content);
      if (matcher.find() && matcher.groupCount() >= 1) {
        return matcher.group(1);
      }
      return null;
    }).filter(group -> group != null).findFirst().map(uri -> URI.create(uri));
  }

  private PageHeaderIntrospector() {
  }
}
