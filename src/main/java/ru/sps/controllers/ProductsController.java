package ru.sps.controllers;

import org.springframework.web.bind.annotation.*;
import ru.sps.api.ProductsApi;

@RestController
public class ProductsController {

    private final ProductsApi productsApi;

    public ProductsController(ProductsApi productsApi) {
        this.productsApi = productsApi;
    }

    @GetMapping("/demands")
    public void sendMessage() {
        productsApi.calculateDemandsAndSendMessage();
    }
}
