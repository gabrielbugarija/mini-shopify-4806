package org.example;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

/**
 * Business logic for shops.
 */
@Service
public class ShopService {

    private final ShopRepo shopRepo;

    public ShopService(ShopRepo shopRepo) {
        this.shopRepo = shopRepo;
    }

    /**
     * Creates a shop.
     */

    @Transactional
    public Shop createShop(String name, String description, String tagsText) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Shop name is required");
        }
        Shop shop = new Shop(name.trim(), description == null ? "" : description.trim());
        parseTags(tagsText).forEach(shop::addTag);
        try {
            return shopRepo.save(shop);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause() instanceof ConstraintViolationException violation
                    && "PUBLIC.UX_SHOP_NAME".equalsIgnoreCase(violation.getConstraintName())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "A shop with that name already exists.", ex);
            }
            throw ex;
        }
    }

    public List<Shop> getAllShops() {
        return shopRepo.findAll();
    }

    /**
     * Finds shops whose name, description, or tags contain the query (case-insensitive).
     * A blank or null query returns every shop.
     */
    public List<Shop> search(String query) {
        if (query == null || query.isBlank()) {
            return getAllShops();
        }
        String q = query.trim().toLowerCase();
        return shopRepo.findAll().stream()
                .filter(shop -> contains(shop.getName(), q)
                        || contains(shop.getDescription(), q)
                        || shop.getTags().stream().anyMatch(tag -> contains(tag, q)))
                .toList();
    }

    private static boolean contains(String text, String lowerQuery) {
        return text != null && text.toLowerCase().contains(lowerQuery);
    }

    public Shop getShop(Long id) {
        return shopRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No shop with id " + id));
    }

    static List<String> parseTags(String tagsText) {
        if (tagsText == null) {
            return List.of();
        }
        return Arrays.stream(tagsText.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(t -> !t.isEmpty())
                .distinct()
                .toList();
    }

    @Transactional
    public Product addProductToShop(Long shopId, String name, double price, int quantity) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name is required");
        }
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }
        Shop shop = getShop(shopId);
        Product product = new Product(name.trim(), price, quantity);
        shop.addProduct(product);
        shopRepo.save(shop);
        return product;
    }
}
