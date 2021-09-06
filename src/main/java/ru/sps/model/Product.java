package ru.sps.model;

import javax.persistence.*;

@Entity
@Table(name = "prod_products")
public class Product extends BaseEntity {

    private static int MAX_DEMAND_WEIGHT = 5;
    private static int BORDER_DEMAND_WEIGHT = 2;

    public final static String TITLE_PROPERTY = "title";

    @Column(name="title", nullable = false)
    private String title;

    @Column(name="is_active", nullable = false)
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private ProductGroup group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uom_id")
    private UnitOfMeasure uom;

    @Column(name = "demand_weight", nullable = false)
    private int demandWeight;

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

    public ProductGroup getGroup() {
        return group;
    }

    public void setGroup(ProductGroup group) {
        this.group = group;
    }

    public UnitOfMeasure getUom() {
        return uom;
    }

    public String getUomShortName() {
        if (this.uom != null) {
            return this.uom.getShortName();
        }
        return "";
    }

    public void setUom(UnitOfMeasure uom) {
        this.uom = uom;
    }

    public int getDemandWeight() {
        return demandWeight;
    }

    public void setDemandWeight(int demandWeight) {
        this.demandWeight = demandWeight;
    }

    public boolean isMaxDemandWeight() {
        return demandWeight >= MAX_DEMAND_WEIGHT;
    }

    public boolean includeInDemandByWeight() {
        return demandWeight > BORDER_DEMAND_WEIGHT;
    }
}
