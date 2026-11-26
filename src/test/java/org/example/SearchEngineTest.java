package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** Unit tests for the shopper shop search in ShopService. */
class SearchEngineTest {

    ShopService shopService;
    Shop furnitureStore;
    Shop bookStore;
    Shop electronicsStore;

    @BeforeEach
    void setUp() {

        // Mock repo so the search runs without a database
        ShopRepo shopRepo = mock(ShopRepo.class);
        shopService = new ShopService(shopRepo);

        // Furniture and Book share the "bookshelf" tag.
        // Furniture and Electronics share "home" in their descriptions.
        furnitureStore = new Shop("Furniture Store", "Beds, desks and shelving for your home");
        furnitureStore.addTag("beds");
        furnitureStore.addTag("bookshelf");
        bookStore = new Shop("Book Store", "New and used books");
        bookStore.addTag("book");
        bookStore.addTag("bookshelf");
        electronicsStore = new Shop("Electronics Store", "Computers, TVs and smart home gadgets");
        electronicsStore.addTag("computers");
        electronicsStore.addTag("tv");

        when(shopRepo.findAll()).thenReturn(List.of(furnitureStore, bookStore, electronicsStore));
    }

    @Test
    void partialMatchIgnoresCaseAcrossNamesAndTags() {
        // "Book Store" matches by name, "Furniture Store" by its "bookshelf" tag
        assertEquals(List.of(furnitureStore, bookStore), shopService.search("BOOK"));
    }

    @Test
    void sharedTagReturnsEveryShopWithThatTag() {
        assertEquals(List.of(furnitureStore, bookStore), shopService.search("bookshelf"));
    }

    @Test
    void sharedDescriptionWordReturnsEveryMatchingShop() {
        assertEquals(List.of(furnitureStore, electronicsStore), shopService.search("home"));
    }

    @Test
    void shopMatchingByTagAndDescriptionAppearsOnce() {
        // "computers" is both a tag and in the description of the Electronics Store
        assertEquals(List.of(electronicsStore), shopService.search("computers"));
    }

    @Test
    void commonNameWordReturnsAllShops() {
        assertEquals(List.of(furnitureStore, bookStore, electronicsStore), shopService.search("store"));
    }

    @Test
    void surroundingWhitespaceIsIgnored() {
        assertEquals(List.of(electronicsStore), shopService.search("   tv   "));
    }

    @Test
    void blankQueryReturnsAllShops() {
        assertEquals(List.of(furnitureStore, bookStore, electronicsStore), shopService.search("  "));
    }

    @Test
    void noMatchReturnsEmptyList() {
        assertTrue(shopService.search("cars").isEmpty());
    }
}
