package ru.sps.integration.users.command;

import org.springframework.stereotype.Component;
import ru.sps.services.OrderService;
import ru.sps.services.ProductService;


@Component
public class UnconfirmedOrdersCommand implements Command {

    private final ProductService productService;
    private final OrderService orderService;

    public UnconfirmedOrdersCommand(ProductService productService, OrderService orderService) {
        this.productService = productService;
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
        var orders =  orderService.getUnconfirmedOrders();

        String result;
        if (orders.isEmpty()) {
            result = "No unconfirmed orders found";
        } else {
            result = productService.getOrderedProductTitlesFromOrders(orders);
        }

        return result;
    }
}
