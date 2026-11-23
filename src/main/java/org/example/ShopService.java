package org.example;

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
        return shopRepo.save(shop);
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
}
