package ru.sps.services;

import liquibase.util.StringUtils;
import org.springframework.stereotype.Service;
import ru.sps.integration.Messenger;
import ru.sps.model.Product;
import ru.sps.repository.ProductRepository;
import ru.sps.integration.viber.ViberService;

import java.math.BigDecimal;
import java.util.*;

import static ru.sps.repository.specification.ProductSpecification.activeProducts;
import static java.util.stream.Collectors.toList;

@Service
public class ProductService {

    private static final BigDecimal SAFETY_CONSUMED_PART = BigDecimal.valueOf(0.9);


    private final ProductRepository productRepository;
    private final OrderService orderService;
    private final Messenger messenger;

    public ProductService(ProductRepository productRepository,
                          Messenger viberService,
                          OrderService orderService) {
        this.productRepository = productRepository;
        this.messenger = viberService;
        this.orderService = orderService;
    }

    public void calculateDemandsAndSendMessage() {
        var productsForOrder = calculateDemands();
        if (StringUtils.isNotEmpty(productsForOrder)) {
            messenger.sendMessage(productsForOrder);
        } else {
            messenger.sendMessage("No demands calculated");
        }
    }

    private String calculateDemands() {
        List<String> demands = new LinkedList<>();
        var activeProducts = productRepository.findAll(activeProducts());
        var ordersMap = orderService.getOrdersForProducts(activeProducts.stream().map(Product::getId).collect(toList()));
        activeProducts.forEach(p -> {
            var consumedPart = orderService.calculateConsumedPart(ordersMap.get(p.getId()));
            if (consumedPart.compareTo(SAFETY_CONSUMED_PART) >= 0) {
                demands.add(p.getTitle());
            }
        });
        return String.join(", ", demands);
    }
}
