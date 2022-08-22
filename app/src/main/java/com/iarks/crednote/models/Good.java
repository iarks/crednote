package com.iarks.crednote.models;

public class Good {
    private int serialNumber;
    private String description;
    private int hsn;
    private int quantity;
    private String unit;
    private double rate;
    private double discountPercentage;
    private double amount;

    public Good(int serialNumber){
        this.serialNumber = serialNumber;
    }

    public Good(int serialNumber, String description, int hsn, int quantity, String unit, float rate, float discountPercentage, float amount) {
        this.serialNumber = serialNumber;
        this.description = description;
        this.hsn = hsn;
        this.quantity = quantity;
        this.unit = unit;
        this.rate = rate;
        this.discountPercentage = discountPercentage;
        this.amount = amount;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getHsn() {
        return hsn;
    }

    public void setHsn(int hsn) {
        this.hsn = hsn;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(float discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public double getAmount() {
        return amount;
    }

}
