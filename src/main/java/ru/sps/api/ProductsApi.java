package ru.sps.api;

import liquibase.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.sps.integration.Messenger;
import ru.sps.services.ProductService;

@Component
@Transactional
public class ProductsApi {

    private final ProductService productService;
    private final Messenger messenger;

    public ProductsApi(ProductService productService, Messenger viberService) {
        this.productService = productService;
        this.messenger = viberService;
    }

    public void calculateDemandsAndSendMessage() {
        var productsForOrder = productService.calculateDemands();
        if (StringUtils.isNotEmpty(productsForOrder)) {
            messenger.sendMessage(productsForOrder);
        } else {
            messenger.sendMessage("No demands calculated");
        }
    }
}
