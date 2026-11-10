package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductServiceTest {

    ProductRepo productRepo;
    ProductService productService;

    Product product1;
    Product product2;
    Product product3;

    @BeforeEach
    void setUp() {
        // Use a mock repo so these tests don't need a database
        productRepo = mock(ProductRepo.class);
        productService = new ProductService(productRepo);

        // Create products
        product1 = new Product("Concert Tickets", 120.00, 2);
        product2 = new Product("Television", 199.99, 1);
        product3 = new Product("E-Book", 14.99, 3);
    }

    @Test
    void createProductWithNullName() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productService.createProduct(null, 120.00, 2)
        );

        assertEquals("Product name is required", exception.getMessage());
    }

    @Test
    void createProductWithBlankName() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productService.createProduct(" ", 120.00, 2)
        );

        assertEquals("Product name is required", exception.getMessage());
    }

    @Test
    void createProductWithNegativePrice() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productService.createProduct("Concert Tickets", -120.00, 2)
        );

        assertEquals("Price cannot be negative", exception.getMessage());
    }

    @Test
    void createProductWithNegativeQuantity() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> productService.createProduct("Concert Tickets", 120.00, -2)
        );

        assertEquals("Quantity cannot be negative", exception.getMessage());
    }

    @Test
    void getAllProducts() {
        when(productRepo.findAll()).thenReturn(List.of(product1, product2, product3));

        List<Product> products = productService.getAllProducts();

        assertEquals(3, products.size());
        assertEquals("Concert Tickets", products.get(0).getProductname());
        assertEquals(199.99, products.get(1).getPrice());
        assertEquals(3, products.get(2).getQuantity());
    }

    @Test
    void getProduct() {
        Long id = 2L;
        when(productRepo.findById(id)).thenReturn(Optional.of(product2));

        Product product = productService.getProduct(id);

        assertEquals(product2, product);
        assertEquals("Television", product.getProductname());
        assertEquals(199.99, product.getPrice());
        assertEquals(1, product.getQuantity());
    }
}
