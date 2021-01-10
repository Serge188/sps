package ru.sps.services;

import org.springframework.stereotype.Service;
import ru.sps.model.Product;
import ru.sps.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.*;

import static ru.sps.repository.specification.ProductSpecification.activeProducts;
import static java.util.stream.Collectors.toList;
import static ru.sps.repository.specification.ProductSpecification.likeTitle;

@Service
public class ProductService {

    private static final BigDecimal SAFETY_CONSUMED_PART = BigDecimal.valueOf(0.9);


    private final ProductRepository productRepository;
    private final OrderService orderService;

    public ProductService(ProductRepository productRepository,
                          OrderService orderService) {
        this.productRepository = productRepository;
        this.orderService = orderService;
    }

    public String calculateDemands() {
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

    public List<Product> getProductsByTitle(String title) {
        return productRepository.findAll(likeTitle(title));
    }
}
