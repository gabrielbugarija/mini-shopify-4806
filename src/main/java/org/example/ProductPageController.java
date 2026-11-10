package org.example;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductPageController {

    private final ProductService productService;

    public ProductPageController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "index";
    }
}
