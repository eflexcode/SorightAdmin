package com.eflexsoft.sorightadmin.model;

public class OrderList {

    private String email;
    private String phoneNumber;
    private String startDate;
    private String endDate;
    private String Package;
    private String name;
    private String id;

    public OrderList(String email, String phoneNumber, String startDate, String endDate, String aPackage, String name, String id) {
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        Package = aPackage;
        this.name = name;
        this.id = id;
    }

    public OrderList() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getPackage() {
        return Package;
    }

    public void setPackage(String aPackage) {
        Package = aPackage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
