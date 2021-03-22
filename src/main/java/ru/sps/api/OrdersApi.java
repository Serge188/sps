package ru.sps.api;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.sps.integration.viber.entries.OrderInput;
import ru.sps.services.OrderService;

import java.util.List;

@Component
@Transactional
public class OrdersApi {

    private final OrderService orderService;

    public OrdersApi(OrderService orderService) {
        this.orderService = orderService;
    }

    public void createOrders(List<OrderInput> inputs) {
        orderService.createOrders(inputs);
    }

    public boolean unconfirmedOrdersExist() {
        return orderService.unconfirmedOrdersExist();
    }
}
