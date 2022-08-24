package com.iarks.crednote.models;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Good {
    public int getSerialNumber() {
        return serialNumber;
    }

    private int serialNumber;
    private String description;
    private int hsn;
    private BigDecimal quantity;
    private String unit;
    private BigDecimal rate;
    private BigDecimal discountPercentage;
    private BigDecimal amount;

    private static BigDecimal percent = new BigDecimal("100.00").setScale(2);

    private boolean isEnableForChange;

    public Good() {
        isEnableForChange = true;
        this.serialNumber = 0;
        this.description = "";
        this.hsn = 0;
        this.quantity = new BigDecimal("0.00").setScale(2, RoundingMode.DOWN);
        this.rate = new BigDecimal("0.00").setScale(2, RoundingMode.DOWN);
        this.unit = "";
        this.discountPercentage = new BigDecimal("0.00").setScale(2, RoundingMode.DOWN);

        goodData = new String[]{
                String.valueOf(serialNumber),
                description,
                String.valueOf(hsn),
                String.valueOf(quantity),
                String.valueOf(rate),
                String.valueOf(unit),
                String.valueOf(discountPercentage)};
    }

    public Good(int serialNumber, String description, int hsn, BigDecimal quantity, String unit, BigDecimal rate, BigDecimal discountPercentage) {
        this.serialNumber = serialNumber;
        this.description = description;
        this.hsn = hsn;
        this.quantity = quantity.setScale(2, RoundingMode.DOWN);
        this.rate = rate.setScale(2, RoundingMode.DOWN);
        this.unit = unit;
        this.discountPercentage = discountPercentage.setScale(2, RoundingMode.DOWN);

        this.isEnableForChange = true;

        goodData = new String[]{
                String.valueOf(serialNumber),
                description,
                String.valueOf(hsn),
                String.valueOf(quantity),
                String.valueOf(rate),
                String.valueOf(unit),
                String.valueOf(discountPercentage)};
    }

    public void lock()
    {
        isEnableForChange = false;
    }

    public void unlock()
    {
        isEnableForChange = true;
    }

    public boolean isLocked()
    {
        return !isEnableForChange;
    }

    // region Setter
    public void setSerialNumber(int serialNumber) {
        if(isEnableForChange)
            this.serialNumber = serialNumber;
    }

    public void setDescription(String description) {
        if(isEnableForChange)
            this.description = description;
    }

    public void setHsn(int hsn) {
        if(isEnableForChange)
            this.hsn = hsn;
    }

    public void setQuantity(BigDecimal quantity) {
        if(isEnableForChange)
            this.quantity = quantity.setScale(2, RoundingMode.DOWN);
    }

    public void setUnit(String unit) {
        if(isEnableForChange)
            this.unit = unit;
    }

    public void setRate(BigDecimal rate) {
        if(isEnableForChange)
            this.rate = rate.setScale(2, RoundingMode.DOWN);
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        if(isEnableForChange)
            this.discountPercentage = discountPercentage.setScale(2, RoundingMode.DOWN);;
    }

    public void setAmount(BigDecimal amount) {
        if(isEnableForChange)
            this.amount = amount.setScale(2, RoundingMode.DOWN);;
    }

    //endregion

    // region getters
    public BigDecimal getAmount() {
        if(isEnableForChange) {
            BigDecimal totalAmt = rate.multiply(quantity).setScale(2, BigDecimal.ROUND_DOWN);
            BigDecimal discountedAmount = discountPercentage.divide(percent).setScale(2, RoundingMode.DOWN).multiply(totalAmt).setScale(2, RoundingMode.DOWN);
            this.amount = totalAmt.subtract(discountedAmount).setScale(2, RoundingMode.DOWN);
            return amount;
        }
        return amount;
    }

    public String getHsnCode() {
        return String.valueOf(this.hsn);
    }

    public BigDecimal getRate() {
        return rate;
    }

    public String getDescriptionOfGoods() {
        return description;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public String getUnit() {
        return unit;
    }

    public BigDecimal getDiscount() {
        return discountPercentage;
    }

    public String getDataAt(int i) {
        if (i >= goodData.length || i < 0)
            return "";
        return goodData[i];
    }
    //endregion

    private final String[] goodData;

}
