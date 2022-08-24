package com.iarks.crednote.models;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class HsnDetails {
    private String hsnCode;
    private BigDecimal taxableAmount;
    private BigDecimal centralTaxRate;
    private BigDecimal centralTaxAmount;
    private BigDecimal stateTaxRate;
    private BigDecimal stateTaxAmount;
    private BigDecimal totalTaxAmount;
    private boolean isEditable;
    private static BigDecimal rateNo = new BigDecimal("100.00").setScale(2, RoundingMode.DOWN);

    public HsnDetails(String hsnCode) {
        isEditable = true;
        this.hsnCode = hsnCode;
        this.stateTaxRate = BigDecimal.ZERO.setScale(2);
        this.centralTaxRate = BigDecimal.ZERO.setScale(2);
        this.stateTaxAmount = BigDecimal.ZERO.setScale(2);
        this.centralTaxAmount = BigDecimal.ZERO.setScale(2);
        this.taxableAmount = BigDecimal.ZERO.setScale(2);
        this.totalTaxAmount = BigDecimal.ZERO.setScale(2);
    }

    public HsnDetails() {
        isEditable = true;
        this.hsnCode = "";
        this.stateTaxRate = BigDecimal.ZERO.setScale(2);
        this.centralTaxRate = BigDecimal.ZERO.setScale(2);
        this.stateTaxAmount = BigDecimal.ZERO.setScale(2);
        this.centralTaxAmount = BigDecimal.ZERO.setScale(2);
        this.taxableAmount = BigDecimal.ZERO.setScale(2);
        this.totalTaxAmount = BigDecimal.ZERO.setScale(2);
    }

    public void lock()
    {
        isEditable = false;
    }

    public void unlock()
    {
        isEditable = true;
    }

    public boolean isLocked()
    {
        return !isEditable;
    }

    public void setHsnCode(String hsnCode) {
        this.hsnCode = hsnCode;
    }

    public void setTaxableAmount(BigDecimal taxableAmount) {
        if(isEditable)
            this.taxableAmount = taxableAmount;
    }

    public void setCentralTaxRate(BigDecimal centralTaxRate) {
        if(isEditable)
            this.centralTaxRate = centralTaxRate.setScale(2);
    }

    public void setCentralTaxAmount(BigDecimal centralTaxAmount) {
        if(isEditable)
            this.centralTaxAmount = centralTaxAmount.setScale(2);
    }

    public void setStateTaxRate(BigDecimal stateTaxRate) {
        if(isEditable)
            this.stateTaxRate = stateTaxRate.setScale(2);
    }

    public void setStateTaxAmount(BigDecimal stateTaxAmount) {
        if(isEditable)
            this.stateTaxAmount = stateTaxAmount.setScale(2);
    }

    public void setTotalTaxAmount(BigDecimal totalTaxAmount) {
        if(isEditable)
            this.totalTaxAmount = totalTaxAmount.setScale(2);
    }

    public String getHsnCode() {
        return hsnCode;
    }

    public BigDecimal getCentralTaxRate() {
        return centralTaxRate;
    }

    public BigDecimal getTaxableAmount() {
        return taxableAmount;
    }

    public BigDecimal getCentralTaxAmount() {
        if(isEditable)
        {
            BigDecimal tax = centralTaxRate
                    .divide(rateNo).setScale(2, RoundingMode.DOWN) // rate divide by hundred
                    .multiply(taxableAmount).setScale(2, RoundingMode.DOWN); // multiply rate with total amount
            return centralTaxAmount = tax;
        }
        return centralTaxAmount;
    }

    public BigDecimal getStateTaxRate() {
        return stateTaxRate;
    }

    public BigDecimal getStateTaxAmount() {
        if(isEditable)
        {
            BigDecimal tax = stateTaxRate
                    .divide(rateNo).setScale(2, RoundingMode.DOWN) // rate divide by hundred
                    .multiply(taxableAmount).setScale(2, RoundingMode.DOWN); // multiply rate with total amount
            return stateTaxAmount = tax;
        }
        return stateTaxAmount;
    }

    public BigDecimal getTotalTaxAmount() {
        if(isEditable)
        {
            return totalTaxAmount = stateTaxAmount.add(centralTaxAmount).setScale(2, RoundingMode.DOWN);
        }
        return totalTaxAmount;
    }
}