package ru.sps.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.sps.model.Order;
import ru.sps.model.Product;

import javax.persistence.criteria.Join;
import java.util.Collection;

public class OrderSpecification {

    public static Specification<Order> ordersForProducts(Collection<Long> productIds) {
        return (r, cq, cb) -> {
            Join<Order, Product> orderProduct = r.join(Order.PRODUCT_PROPERTY);
            return orderProduct.get(Product.ID_PROPERTY).in(productIds);
        };
    }
}
