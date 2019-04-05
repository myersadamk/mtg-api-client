package com.exigentech.mtgapiclient;

import com.exigentech.mtgapiclient.cards.model.CardCatalog;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class CardCatalogueTest {

  @Test
  void printCards(@Autowired CardCatalog catalog) {
    final var flux = catalog.getAllCards();
    flux.take(10).subscribe(System.out::println);
//    flux.toIterable().forEach(System.out::println);
//    final var printCards = new CardCatalog(client).getAllCards().subscribe(System.out::println);
//    assertTrue(printCards.isDisposed());
  }
}