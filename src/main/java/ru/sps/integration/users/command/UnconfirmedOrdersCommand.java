package ru.sps.integration.users.command;

import org.springframework.stereotype.Component;
import ru.sps.services.OrderService;

import java.util.stream.Collectors;

@Component
public class UnconfirmedOrdersCommand implements Command {

    private final OrderService orderService;

    public UnconfirmedOrdersCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String getName() {
        return "/uo";
    }

    @Override
    public String getDescription() {
        return "List all unconfirmed orders";
    }

    @Override
    public String execute(String param) {
        String result =  orderService.getUnconfirmedOrders()
                .stream()
                .map(o -> o.getProduct().getTitle() + ": " + o.getQuantity())
                .collect(Collectors.joining(", "));
        if (result.isBlank()) result = "No unconfirmed orders found";
        return result;
    }
}
