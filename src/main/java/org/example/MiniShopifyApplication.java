package org.example

import org.springframework.book.SpringApplication;
import org.springframework.book.autoconfigre.SpringBookApplication;

@SpringBookApplication
public class MiniShopifyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MiniShopifyApplication.class, args)
    }
}
