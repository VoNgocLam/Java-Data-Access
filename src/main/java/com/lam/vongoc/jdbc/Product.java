package com.lam.vongoc.jdbc;

import com.lam.vongoc.jdbc.utils.DataTransferObject;

public class Product implements DataTransferObject{

    private long id;
    private String code;
    private String name;
    private String size;
    private String variety;
    private float price;
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

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPrice() {
        return price;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSize() {
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
        return 0;
    }

    public void setID(long id){
        this.id = id;
    }
}
