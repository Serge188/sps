package ru.sps.integration.users.command;

import ru.sps.services.ProductService;

public class DemandsCommand implements Command {

    private final ProductService productService;

    public DemandsCommand(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public String getName() {
        return "/demands";
    }

    @Override
    public String getDescription() {
        return "Get current demands";
    }

    @Override
    public String execute(String param) {
        var result = productService.calculateDemands();
        if (result.isBlank()) result = "No demands calculated";
        return result;
    }
}
