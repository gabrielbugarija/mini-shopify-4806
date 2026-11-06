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

    // Temporary: the home page lists every shop. Replaced by search() in the next PR.
    public List<Shop> getAllShops() {
        return shopRepo.findAll();
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
