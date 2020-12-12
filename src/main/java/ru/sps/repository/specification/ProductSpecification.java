package ru.sps.repository.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.sps.model.Product;

public class ProductSpecification {

    public static Specification<Product> activeProducts() {
        return (r, cq, cb) -> cb.equal(r.get("active"), Boolean.TRUE);
    }

//    public static Specification<Product> joinTest(SomeUser input) {
//        return new Specification<Product>() {
//            public Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                Join<Product,SomeUser> userProd = root.join("owner");
//                Join<FollowingRelationship,Product> prodRelation = userProd.join("ownedRelationships");
//                return cb.equal(prodRelation.get("follower"), input);
//            }
//        };
//    }
}
