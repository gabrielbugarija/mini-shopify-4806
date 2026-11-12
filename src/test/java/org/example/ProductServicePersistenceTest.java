package org.example;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
@Transactional
public class ProductServicePersistenceTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    ProductService productService;

    @Autowired
    ShopService shopService;

    @Autowired
    ProductRepo productRepo;

    @Autowired
    ShopRepo shopRepo;

    @Test
    void createProductSavesToDatabase() {
        Product saved = productService.createProduct("  Concert Tickets  ", 120.00, 2);

        assertNotNull(saved.getId());

        entityManager.flush();
        entityManager.clear();

        Product found = productRepo.findById(saved.getId()).orElseThrow();
        assertEquals("Concert Tickets", found.getName());
        assertEquals(120.00, found.getPrice());
        assertEquals(2, found.getQuantity());
        assertEquals(1, productRepo.count());
    }

    @Test
    void createShopSavesToDatabase() {
        Shop saved = shopService.createShop("TechStore", "Electronics", "tech,gadgets");

        assertNotNull(saved.getId());

        entityManager.flush();
        entityManager.clear();

        Shop found = shopRepo.findById(saved.getId()).orElseThrow();
        assertEquals("TechStore", found.getName());
        assertEquals("Electronics", found.getDescription());
        assertEquals(Set.of("tech", "gadgets"), found.getTags());
        assertEquals(1, shopRepo.count());
    }

    @Test
    void savingShopAlsoSavesItsProducts() {
        Shop shop = new Shop("TechStore", "Electronics");
        shop.addProduct(new Product("Laptop", 999.99, 10));
        shop.addProduct(new Product("Mouse", 25.50, 100));
        Shop saved = shopRepo.save(shop);

        entityManager.flush();
        entityManager.clear();

        Shop found = shopRepo.findById(saved.getId()).orElseThrow();
        assertEquals(2, found.getProducts().size());
        assertEquals(2, productRepo.count());
    }

    @Test
    void removingProductFromShopDeletesIt() {
        Shop shop = new Shop("TechStore", "Electronics");
        shop.addProduct(new Product("Laptop", 999.99, 10));
        shop.addProduct(new Product("Mouse", 25.50, 100));
        Shop saved = shopRepo.save(shop);

        entityManager.flush();
        entityManager.clear();

        Shop found = shopRepo.findById(saved.getId()).orElseThrow();
        found.removeProduct(found.getProducts().get(0));

        entityManager.flush();
        entityManager.clear();

        assertEquals(1, shopRepo.findById(saved.getId()).orElseThrow().getProducts().size());
        assertEquals(1, productRepo.count());
    }

    @Test
    void getMissingProductOrShopThrowsNotFound() {
        assertThrows(ResponseStatusException.class, () -> productService.getProduct(-1L));
        assertThrows(ResponseStatusException.class, () -> shopService.getShop(-1L));
    }
}
