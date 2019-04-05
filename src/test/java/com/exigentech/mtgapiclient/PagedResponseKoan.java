package com.exigentech.mtgapiclient;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static reactor.core.publisher.Flux.generate;
import static reactor.core.publisher.Flux.just;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

@Disabled
final class PagedResponseKoan {

  final static class KoanPage {

    private static final String FIRST = "1";
    private static final String LAST = "5";
    private final String data;

    static final KoanPage get() {
      return new KoanPage(FIRST);
    }

    static final KoanPage get(String id) {
      if (id == null) {
        throw new IllegalArgumentException();
      }
      if ("6".equals(id)) {
        throw new IllegalStateException();
      }
      return new KoanPage(String.valueOf(Integer.valueOf(id)));
    }

    final int data() {
      return Integer.valueOf(data);
    }

    final String last() {
      return LAST;
    }

    final String self() {
      return String.valueOf(data);
    }

    final String next() {
      return String.valueOf(Integer.valueOf(data) + 1);
    }

    public KoanPage(String data) {
      this.data = data;
    }
  }

  @Test
  public void initialCall() {
    assertThat(getInitialCall().collect(Collectors.toList()).block(), is(List.of(1)));
  }

  @Test
  public void allCalls() {
    assertThat(getAllCalls().collect(Collectors.toList()).block(), is(List.of(1, 2, 3, 4, 5)));
  }

  private Flux<Integer> getInitialCall() {
    return just(KoanPage.get("")).map(p -> p.data());
  }

  private Flux<Integer> getAllCalls() {
    return generate(KoanPage::get, (state, sink) -> {
          sink.next(state.data());

          if (state.self().equals(state.last())) {
            sink.complete();
            return null;
          }

          final var page = KoanPage.get(state.next());
          return page;
        }
    );
  }
}
