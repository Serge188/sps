package ru.sps.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="msg_users")
public class MessengerUser extends BaseEntity {

    @Column(name = "external_id", nullable = false)
    private String externalId;

    @Column(name = "external_name", nullable = false)
    private String externalName;

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getExternalName() {
        return externalName;
    }

    public void setExternalName(String externalName) {
        this.externalName = externalName;
    }
}
