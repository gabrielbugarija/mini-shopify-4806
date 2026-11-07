package org.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MiniShopifyController {
    @GetMapping("/")
    public String home() {
        return "Mini Shopify is running";
    }
}