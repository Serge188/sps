package ru.sps.integration.viber.entries;

import java.math.BigDecimal;

public class OrderInput {
    private long productId;
    private BigDecimal qty;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }
}
