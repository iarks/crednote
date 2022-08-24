package com.iarks.crednote.models;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class GoodLineItem extends GoodLine {

    private String description;
    private BigDecimal amount;
    public final String KEY;

    public GoodLineItem(String key, String description, BigDecimal amount)
    {
        this.KEY = key;
        this.description = description;
        this.amount = amount.setScale(2, RoundingMode.DOWN);
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String getPrintableDescription()
    {
        return getDescription();
    }

    @Override
    public String getPrintableHsn() {
        return "";
    }

    @Override
    public String getPrintableQuantity() {
        return "";
    }

    @Override
    public String getPrintableRate() {
        return "";
    }

    @Override
    public String getPrintableUnit() {
        return "";
    }

    @Override
    public String getPrintableDiscountRate() {
        return "";
    }

    @Override
    public String getPrintableAmount()
    {
        return String.valueOf(getAmount());
    }

}
