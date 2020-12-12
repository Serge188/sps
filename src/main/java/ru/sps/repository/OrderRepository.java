package ru.sps.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sps.model.Order;

@Repository
public interface OrderRepository  extends CrudRepository<Order, Long>, JpaSpecificationExecutor<Order> {
}
