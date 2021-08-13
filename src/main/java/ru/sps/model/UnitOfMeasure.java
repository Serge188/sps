package ru.sps.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "prod_uom")
public class UnitOfMeasure extends BaseEntity {

    @Column(name="short_name", nullable = false)
    private String shortName;

    @Column(name="full_name")
    private String fullName;

    public UnitOfMeasure() {
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
