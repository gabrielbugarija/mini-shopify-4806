package org.example;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public Product createProduct(
            @RequestParam String name,
            @RequestParam double price,
            @RequestParam int quantity) {

        return productService.createProduct(name, price, quantity);
    }

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productService.getProduct(id);
    }

    @GetMapping("/page")
    public ModelAndView productsPage() {
        ModelAndView model = new ModelAndView("products");
        model.addObject("products", productService.getAllProducts());
        return model;
    }

    @GetMapping("/{id}/page")
    public ModelAndView productPage(@PathVariable Long id) {
        ModelAndView model = new ModelAndView("product-details");
        model.addObject("product", productService.getProduct(id));
        return model;
    }
}