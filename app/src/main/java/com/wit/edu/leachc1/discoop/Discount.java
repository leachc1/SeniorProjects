package com.wit.edu.leachc1.discoop;

/**
 * Created by kanners on 7/12/2018.
 */

public class Discount {
    private int id;
    private String type;
    private String address;
    private String expiration;
    private String details;

    public Discount() {
    }

    public Discount(int id, String type, String address, String details) {
        this.id = id;
        this.type = type;
        this.address = address;
        this.details = details;
    }

    //Setters
    public void setId(int id) {
        this.id = id;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setDetails(String details) {
        this.details = details;
    }

    //Getters
    public int getId() {
        return id;
    }
    public String getType() {
        return type;
    }
    public String getAddress() {
        return address;
    }
    public String getDetails() {
        return details;
    }
}
