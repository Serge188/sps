package ru.sps.integration.users.command;

import org.springframework.stereotype.Component;
import ru.sps.model.Product;
import ru.sps.services.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SearchProductCommand implements Command {

    private final ProductService productService;

    public SearchProductCommand(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public String getName() {
        return "/s";
    }

    @Override
    public String getDescription() {
        return "Search products by title";
    }

    @Override
    public String execute(String param) {
        List<Product> products = productService.getProductsByTitle(param);
        if (!products.isEmpty()) return products.stream().map(Product::getTitle).collect(Collectors.joining(", "));
        return String.format("Products with name like %s not found", param);
    }
}
