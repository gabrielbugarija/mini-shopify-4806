package org.example;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/shops")
public class ShopController {

    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping
    public List<Shop> getShops() {
        return shopService.getAllShops();
    }

    @GetMapping
    public Shop createShop(
            @RequestParam String name,
            @RequestParam String description,
            @RequestParam String tagsText){

        return shopService.createShop(name, description, tagsText);
        }
    @GetMapping
    public Shop getShop(
            @RequestParam Long id
    ){
        return shopService.getShop(id);
    }

}
