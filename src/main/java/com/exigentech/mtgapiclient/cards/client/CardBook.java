package com.exigentech.mtgapiclient.cards.client;

import static java.util.Arrays.stream;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.springframework.web.util.UriComponentsBuilder;

public final class CardBook {

  private final int pageCount;
  private final URI baseURI;

  public CardBook(int pageCount, URI baseURI) {
    this.pageCount = pageCount;
    this.baseURI = baseURI;
  }

  public static CardBook create(CardPage page, URI baseURI) {
    final var lastPageParam = stream(page.last().getQuery().split("&"))
        .collect(
            Collectors.toMap(s ->
                    s.substring(0, s.indexOf("=")),
                s -> s.substring(s.indexOf("="))
            )
        ).get("page");

    if (lastPageParam == null) {
      // TODO
    }

    return new CardBook(Integer.valueOf(lastPageParam), baseURI);
  }

  public List<URI> range(int first, int last) {
    return IntStream.range(0, pageCount).mapToObj(this::constructURI).collect(Collectors.toList());
  }

  private URI constructURI(final int pageNumber) {
    return UriComponentsBuilder.fromUri(baseURI).queryParam("page", pageNumber).build().toUri();
  }
}
