package com.lam.vongoc.jdbc;

import com.lam.vongoc.jdbc.utils.DataTransferObject;

import java.math.BigDecimal;

public class Product implements DataTransferObject{

    private long productId;
    private String code;
    private String name;
    private int size;
    private String variety;
    private BigDecimal price;
    private String status;

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public String getVariety() {
        return variety;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }


    @Override
    public long getId() {
        return productId;
    }

    public void setID(long id){
        this.productId = id;
    }

    @Override
    public String toString() {
        return "Product{" +
                "Id=" + productId +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", size='" + size + '\'' +
                ", variety='" + variety + '\'' +
                ", price=" + price +
                ", status='" + status + '\'' +
                '}';
    }
}
