package ru.sps.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.sps.model.Product;

public class ProductSpecification {

    public static Specification<Product> activeProducts() {
        return (r, cq, cb) -> cb.equal(r.get("active"), Boolean.TRUE);
    }

    public static Specification<Product> likeTitle(String title) {
        var paramTitle = '%' + title + '%';
        return (r, cq, cb) -> cb.like(cb.lower(r.get(Product.TITLE_PROPERTY)), paramTitle.toLowerCase());
    }
}
