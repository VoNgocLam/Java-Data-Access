package com.lam.vongoc.jdbc;

import com.lam.vongoc.jdbc.utils.DataTransferObject;

public class OrderItem implements DataTransferObject {

    private long id;
    private String orderId;
    private String productId;
    private String quantity;

    @Override
    public long getId() {
        return 0;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
