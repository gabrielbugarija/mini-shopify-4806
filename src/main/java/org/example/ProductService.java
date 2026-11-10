package org.example;

import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepo productRepo;

    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    @Transactional
    public Product createProduct(String name, double price, int quantity) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name is required");
        }

        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }

        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        Product product = new Product(name.trim(), price, quantity);

        return productRepo.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepo.findAll();
    }

    public Product getProduct(Long id) {
        return productRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "No product with id " + id
                ));
    }
}