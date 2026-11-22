package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ShopServiceTest {

    ShopRepo shopRepo;
    ShopService shopService;

    @BeforeEach
    void setUp() {
        // Use a mock repo so these tests don't need a database
        shopRepo = mock(ShopRepo.class);
        shopService = new ShopService(shopRepo);
    }

    @Test
    void createShop() {
        when(shopRepo.save(any(Shop.class))).thenAnswer(call -> call.getArgument(0));

        Shop shop = shopService.createShop("  Shop 1  ", "  Sells everything  ", "Books, Games");

        assertEquals("Shop 1", shop.getName());
        assertEquals("Sells everything", shop.getDescription());
        assertEquals(Set.of("books", "games"), shop.getTags());
    }

    @Test
    void createShopWithBlankName() {
        assertThrows(IllegalArgumentException.class,
                () -> shopService.createShop("  ", "Sells everything", "books"));
    }

    @Test
    void getAllShops() {
        Shop shop1 = new Shop("Shop 1");
        Shop shop2 = new Shop("Shop 2");
        when(shopRepo.findAll()).thenReturn(List.of(shop1, shop2));

        assertEquals(List.of(shop1, shop2), shopService.getAllShops());
    }

    @Test
    void getShop() {
        Shop shop = new Shop("Shop 1");
        when(shopRepo.findById(1L)).thenReturn(Optional.of(shop));

        assertEquals(shop, shopService.getShop(1L));
    }

    @Test
    void getShopNotFound() {
        when(shopRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class, () -> shopService.getShop(1L));
    }

    @Test
    void parseTags() {
        // Trims, lowercases, and removes empty and duplicate tags
        assertEquals(List.of("books", "games"), ShopService.parseTags(" Books, games,,BOOKS "));
    }

    @Test
    void parseTagsWithNull() {
        assertTrue(ShopService.parseTags(null).isEmpty());
    }
}
