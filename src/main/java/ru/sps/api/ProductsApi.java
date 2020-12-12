package ru.sps.api;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.sps.services.ProductService;

@Component
@Transactional
public class ProductsApi {

    private final ProductService productService;

    public ProductsApi(ProductService productService) {
        this.productService = productService;
    }

    public void calculateDemandsAndSendMessage() {
        productService.calculateDemandsAndSendMessage();
    }
}
