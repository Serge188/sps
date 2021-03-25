package ru.sps.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "prod_groups")
public class ProductGroup  extends BaseEntity {

    @Column(name="order_number", nullable = false)
    private int orderNumber;

    @Column(name="title", nullable = false)
    private String title;

    public ProductGroup() {
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
