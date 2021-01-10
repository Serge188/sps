package ru.sps.integration.users.command;

import org.springframework.stereotype.Component;
import ru.sps.services.OrderService;

@Component
public class RemoveUnconfirmedOrdersCommand implements Command {

    private final OrderService orderService;

    public RemoveUnconfirmedOrdersCommand(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public String getName() {
        return "/rmo";
    }

    @Override
    public String getDescription() {
        return "Remove unconfirmed purchases";
    }

    @Override
    public String execute(String param) {
        orderService.removeUnconfirmedOrders();
        return "Orders removed";
    }
}
