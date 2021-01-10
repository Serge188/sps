package ru.sps.integration.users.command;

import org.springframework.stereotype.Component;
import ru.sps.services.OrderService;

@Component
public class ConfirmOrdersCommand implements Command {

    private final OrderService orderService;

    public ConfirmOrdersCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String getName() {
        return "/co";
    }

    @Override
    public String getDescription() {
        return "Move orders to status 'CONFIRMED'";
    }

    @Override
    public String execute(String param) {
        orderService.confirmOrders();
        return "Confirmed";
    }
}
