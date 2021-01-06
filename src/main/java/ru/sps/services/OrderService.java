package ru.sps.services;

import org.springframework.stereotype.Service;
import ru.sps.integration.viber.entries.OrderInput;
import ru.sps.model.Order;
import ru.sps.repository.OrderRepository;
import ru.sps.repository.ProductRepository;
import ru.sps.utils.NumberUtils;
import ru.sps.utils.PeriodHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

import static ru.sps.repository.specification.OrderSpecification.ordersForProducts;

@Service
public class OrderService {

    private static final int MINIMAL_ORDERS_COUNT_FOR_CALCULATION = 5;

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public List<Order> createOrders(List<OrderInput> inputs) {

        var orders = new ArrayList<Order>();
        var now = new Date();
        inputs.forEach(input -> {
            var order = new Order();
            productRepository.findById(input.getProductId()).ifPresent(p -> {
                order.setProduct(p);
                order.setQuantity(input.getQty());
                order.setDate(now);
                orders.add(order);
            });
        });
        orderRepository.saveAll(orders);
        return orders;
    }

    Map<Long, List<Order>> getOrdersForProducts(Collection<Long> productIds) {
        Map<Long, List<Order>> map = new HashMap<>();
        var orders = orderRepository.findAll(ordersForProducts(productIds));
        orders.forEach(order -> {
            var productId = order.getProduct().getId();
            map.putIfAbsent(productId, new ArrayList<>());
            map.get(productId).add(order);
        });
        return map;
    }

    public BigDecimal calculateConsumedPart(List<Order> orders) {
        if (orders != null && orders.size() >= MINIMAL_ORDERS_COUNT_FOR_CALCULATION) {
            var now = new Date();
            var orderDates = new ArrayList<Date>();
            var orderAmounts = new ArrayList<BigDecimal>();
            orders.forEach(o -> {
                orderDates.add(o.getDate());
                orderAmounts.add(o.getQuantity());
            });
            var medianPeriod = NumberUtils.calculateMedianValue(PeriodHelper.calculatePeriodsInDays(orderDates));
            var medianAmount = NumberUtils.calculateMedianValue(orderAmounts);
            var lastOrder = orders.stream().max(Comparator.comparing(Order::getDate));

            if (medianPeriod != null && medianPeriod.compareTo(BigDecimal.ZERO) > 0 && lastOrder.isPresent()) {
                var consumption = medianAmount.divide(medianPeriod, 3, RoundingMode.HALF_UP);
                var daysFromLastOrder = PeriodHelper.calculatePeriodBetweenDates(lastOrder.get().getDate(), now);
                return consumption
                        .multiply(BigDecimal.valueOf(daysFromLastOrder))
                        .divide(lastOrder.get().getQuantity(), 3, RoundingMode.HALF_UP);
            }
        }
        return BigDecimal.ZERO;
    }
}
