package org.example;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductPageController {

    private final ShopService shopService;
    private final ProductService productService;

    public ProductPageController(ShopService shopService, ProductService productService)
    {
        this.shopService = shopService;
        this.productService = productService;
    }

    @GetMapping("/")
    public String home(@RequestParam(name = "q", required = false) String query, Model model) {
        model.addAttribute("shops", shopService.search(query));
        model.addAttribute("query", query == null ? "" : query.trim());
        return "index";
    }

    @GetMapping("/merchant")
    public String merchant() {
        return "merchant";
    }

    @PostMapping("/products/{id}/purchase-page")
    public String purchaseProductFromPage(
            @PathVariable Long id,
            @RequestParam int amount) {

        productService.purchaseProduct(id, amount);

        return "redirect:/products/" + id + "/page";
    }
}
