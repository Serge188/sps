package ru.sps.api;

import org.springframework.stereotype.Component;
import ru.sps.integration.Messenger;
import ru.sps.integration.viber.entries.IncomingMessageEntry;
import ru.sps.services.OrderService;

import java.util.stream.Collectors;

@Component
public class MessageApi {

    private final Messenger viberService;
    private final OrderService orderService;

    public MessageApi(Messenger viberService,
                      OrderService orderService) {
        this.viberService = viberService;
        this.orderService = orderService;
    }

    public void retrieveMessage(IncomingMessageEntry message) {
        var orderInputs = viberService.retrieveMessage(message);
        var orders = orderService.createOrders(orderInputs);
        viberService.sendMessage(orders.stream().map(o -> o.getProduct().getTitle()).collect(Collectors.joining(", ")));
    }
}
