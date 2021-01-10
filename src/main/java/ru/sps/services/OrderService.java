package ru.sps.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.sps.integration.viber.entries.OrderInput;
import ru.sps.model.Order;
import ru.sps.model.OrderStatus;
import ru.sps.repository.OrderRepository;
import ru.sps.repository.ProductRepository;
import ru.sps.utils.NumberUtils;
import ru.sps.utils.PeriodHelper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static ru.sps.repository.specification.OrderSpecification.ordersForProducts;
import static ru.sps.repository.specification.OrderSpecification.unconfirmedOrders;
import static ru.sps.repository.specification.ProductSpecification.likeTitle;

@Service
public class OrderService {

    private static final int MINIMAL_ORDERS_COUNT_FOR_CALCULATION = 5;

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    private Logger log = LoggerFactory.getLogger(OrderService.class);

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public String createOrdersFromUserInput(String userText) {
        var inputs = new ArrayList<OrderInput>();
        var unreadable = new ArrayList<String>();
        var unreadableTitle = new ArrayList<String>();
        var unreadableQty = new ArrayList<String>();
        var resultList = new ArrayList<String>();

        List<String> titleQtyBlocks = Arrays.asList(userText.split(", "));

        titleQtyBlocks.forEach(block -> {
            var pair = block.split(":");
            if (pair.length != 2) unreadable.add(block);
            var product = productRepository.findAll(likeTitle(pair[0])).stream().findAny();
            var qty = BigDecimal.ZERO;
            try {
                qty = new BigDecimal(pair[1]);
            } catch (NumberFormatException e) {
                log.error("Error while parsing qty: " + pair[1]);
            }
            if (!product.isPresent()) {
                unreadableTitle.add(block);
            } else if (qty.compareTo(BigDecimal.ZERO) == 0) {
                unreadableQty.add(block);
            } else {
                inputs.add(new OrderInput(product.get().getId(), qty));
            }
        });
        if (!unreadable.isEmpty()) resultList.add(("Unreadable: " + String.join(", ", unreadable)));
        if (!unreadableTitle.isEmpty()) resultList.add(("Unreadable title: " + String.join(", ", unreadableTitle)));
        if (!unreadableQty.isEmpty()) resultList.add(("Unreadable qty: " + String.join(", ", unreadableQty)));

        var orders = createOrders(inputs);
        if (!orders.isEmpty()) resultList.add((
                "Accepted:"
                        + orders
                        .stream()
                        .map(o -> o.getProduct().getTitle())
                        .collect(Collectors.joining(", "))));
        return String.join(", ", resultList);
    }

    public List<Order> createOrders(List<OrderInput> inputs) {

        var orders = new ArrayList<Order>();
        var unconfirmedOrderProductIds = getUnconfirmedOrders().stream().map(o -> o.getProduct().getId()).collect(Collectors.toSet());
        var now = new Date();
        inputs.forEach(input -> {
            var order = new Order();
            productRepository.findById(input.getProductId()).ifPresent(p -> {
                if (!unconfirmedOrderProductIds.contains(p.getId())) {
                    order.setProduct(p);
                    order.setQuantity(input.getQty());
                    order.setDate(now);
                    order.setStatus(OrderStatus.NEW);
                    orders.add(order);
                }
            });
        });
        orderRepository.saveAll(orders);
        return orders;
    }

    public void confirmOrders() {
        var unconfirmedOrders = getUnconfirmedOrders();
        unconfirmedOrders.forEach(o -> o.setStatus(OrderStatus.CONFIRMED));
    }

    public void removeUnconfirmedOrders() {
        getUnconfirmedOrders().forEach(orderRepository::delete);
    }

    public List<Order> getUnconfirmedOrders() {
        return orderRepository.findAll(unconfirmedOrders());
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
