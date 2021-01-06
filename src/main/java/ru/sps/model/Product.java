package ru.sps.model;

import javax.persistence.*;

@Entity
@Table(name = "prod_products")
public class Product extends BaseEntity {

    public final static String TITLE_PROPERTY = "title";

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="is_active", nullable = false)
    private boolean active;

    public Product() {
    }

    public Product(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
