package org.example;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductPageController {

    private final ShopService shopService;

    public ProductPageController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("shops", shopService.getAllShops());
        return "index";
    }
}
