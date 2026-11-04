package org.example;

import jakarta.persistence.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Shop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL,
            orphanRemoval = true)

    private List<Product> products = new ArrayList<>();

    protected Shop() {} // Required by JPA

    public Shop(String name) {
        this.name = name;
    }

    public void addProduct(Product product) {
        products.add(product);
    }

    public void removeProduct(Product product) {
        products.remove(product);
    }

    public List<Product> getProducts() {
        return products;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
