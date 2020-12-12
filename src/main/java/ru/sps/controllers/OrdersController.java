package ru.sps.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.sps.api.OrdersApi;
import ru.sps.integration.viber.entries.OrderInput;

import java.util.List;

@RestController
public class OrdersController {

    private final OrdersApi ordersApi;

    public OrdersController(OrdersApi ordersApi) {
        this.ordersApi = ordersApi;
    }

    @PostMapping("/orders")
    public void createOrders(@RequestBody List<OrderInput> inputs) {
        ordersApi.createOrders(inputs);
    }
}
