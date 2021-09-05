package ru.sps.services;

import org.springframework.stereotype.Service;
import ru.sps.model.Order;
import ru.sps.model.Product;
import ru.sps.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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

    public String getDemandsAsString() {
        List<Product> demands = calculateDemands();
        return getOrderedProductTitles(demands);
    }

    private List<Product> calculateDemands() {
        List<Product> demands = new LinkedList<>();
        var activeProducts = productRepository.findAll(activeProducts());
        var ordersMap = orderService.getOrdersForProducts(activeProducts.stream().map(Product::getId).collect(toList()));
        activeProducts.forEach(p -> {
            var consumedPart = orderService.calculateConsumedPart(ordersMap.get(p.getId()));
            if (consumedPart.compareTo(SAFETY_CONSUMED_PART) >= 0) {
                demands.add(p);
            }
        });
        return demands;
    }

    public List<Product> getProductsByTitle(String title) {
        return productRepository.findAll(likeTitle(title));
    }

    private String getOrderedProductTitles(List<Product> products) {
        return getProductTitlesOrderedByGroups(products, null);
    }

    public String getOrderedProductTitlesFromOrders(List<Order> orders) {
        var products = orders.stream().map(Order::getProduct).collect(Collectors.toList());
        var map = orders.stream().collect(Collectors.toMap(o -> o.getProduct().getId(), o -> o));
        return getProductTitlesOrderedByGroups(products, map);
    }

    private String getProductTitlesOrderedByGroups(List<Product> products, Map<Long, Order> prodToOrdersMap) {
        var productsWithoutGroup = new ArrayList<Product>();
        var groupMap = new HashMap<Integer, List<Product>>();

        products.forEach(p -> {
            if (p.getGroup() == null) {
                productsWithoutGroup.add(p);
            } else {
                groupMap.putIfAbsent(p.getGroup().getOrderNumber(), new ArrayList<>());
                groupMap.get(p.getGroup().getOrderNumber()).add(p);
            }
        });

        StringBuilder sb = new StringBuilder();

        groupMap.keySet().stream().sorted().forEach(gOrder -> {
            groupMap.get(gOrder).forEach(p ->
                    sb
                            .append(p.getTitle())
                            .append(prodToOrdersMap != null ? ": " : "")
                            .append(prodToOrdersMap != null
                                    ? prodToOrdersMap.get(p.getId()).getQuantity()
                                    : "")
                            .append(prodToOrdersMap != null ? " " : "")
                            .append(prodToOrdersMap != null ? p.getUomShortName() : "")
                            .append("\n"));
            sb.append("\n");
        });

        productsWithoutGroup.forEach(p -> sb.append(p.getTitle()).append("\n"));

        return sb
                .toString()
                .replaceAll("$\n", "")
                .replaceAll("$\n", "");
    }
}
