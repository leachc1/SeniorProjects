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
    private String name;

    public Discount() {
    }

    public Discount(int id, String type, String address, String expiration, String details, String name) {
        this.id = id;
        this.type = type;
        this.address = address;
        this.expiration = expiration;
        this.details = details;
        this.name = name;
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
    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }
    public void setDetails(String details) {
        this.details = details;
    }
    public void setName(String name) {
        this.name = name;
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
    public String getExpiration() {
        return expiration;
    }
    public String getDetails() {
        return details;
    }
    public String getName() {
        return name;
    }
}
