package ru.sps.integration.users.command;

import org.springframework.stereotype.Component;
import ru.sps.services.ProductService;

@Component
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
        var result = productService.getDemandsAsString();
        if (result.isBlank()) result = "No demands calculated";
        return result;
    }
}
