package com.exigentech.mtgapiclient;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class CardCatalogueTest {

  @Test
  void lol(@Autowired CardCatalog catalog) {
    final var flux = catalog.getAllCards();
    flux.take(1000).subscribe(System.out::println);
//    flux.toIterable().forEach(System.out::println);
//    final var lol = new CardCatalog(client).getAllCards().subscribe(System.out::println);
//    assertTrue(lol.isDisposed());
  }
}