package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    // Create product
    Product product = new Product("Concert Tickets", 120.00, 2);

    // Create empty product to test default constructor and setter methods
    Product emptyProduct = new Product();

    @Test
    void getProductname() {
        assertEquals("Concert Tickets", product.getName());
    }

    @Test
    void setProductname() {
        emptyProduct.setName("Keychain");
        assertEquals("Keychain", emptyProduct.getName());
    }

    @Test
    void getPrice() {
        assertEquals(120.00, product.getPrice());
    }

    @Test
    void setPrice() {
        emptyProduct.setPrice(49.99);
        assertEquals(49.99, emptyProduct.getPrice());
    }

    @Test
    void getQuantity() {
        assertEquals(2, product.getQuantity());
    }

    @Test
    void setQuantity() {
        emptyProduct.setQuantity(3);
        assertEquals(3, emptyProduct.getQuantity());
    }
}