package org.example;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
    public String merchant(Model model) {
        model.addAttribute("shops", shopService.getAllShops());
        return "merchant";
    }

    @GetMapping("/merchant/shops/create")
    public String createShopPage() {
        return "create-shop";
    }

    @PostMapping("/merchant/shops/create")
    public String createShop(
            @RequestParam String name,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String tagsText) {
        shopService.createShop(name, description, tagsText);
        return "redirect:/merchant";
    }

    @GetMapping("/merchant/shops/{id}/products")
    public String manageProducts(@PathVariable Long id, Model model) {
        Shop shop = shopService.getShop(id);
        model.addAttribute("shop", shop);
        model.addAttribute("products", shop.getProducts());
        return "shop-products";
    }

    @PostMapping("/merchant/shops/{id}/products")
    public String addProduct(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam double price,
            @RequestParam int quantity) {
        shopService.addProductToShop(id, name, price, quantity);
        return "redirect:/merchant/shops/" + id + "/products";}

    @PostMapping("/products/{id}/purchase-page")
    public String purchaseProductFromPage(
            @PathVariable Long id,
            @RequestParam int amount) {

        productService.purchaseProduct(id, amount);

        return "redirect:/products/" + id + "/page";
    }
}
