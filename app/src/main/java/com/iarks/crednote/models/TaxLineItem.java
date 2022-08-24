package com.iarks.crednote.models;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TaxLineItem extends TaxLine {
    private final String column0;
    private final BigDecimal taxableAmount;
    private final BigDecimal totalCentralTaxAmount;
    private final BigDecimal totalStateTaxAmount;
    private final BigDecimal totalTaxAmount;

    public final String KEY;

    public TaxLineItem(String key, String column0, BigDecimal taxableAmount, BigDecimal totalCentralTaxAmount, BigDecimal totalStateTaxAmount, BigDecimal totalTaxAmount) {
        this.KEY = key;
        this.column0 = column0 == null ? "" : column0;
        this.taxableAmount = taxableAmount.setScale(2, RoundingMode.DOWN);
        this.totalCentralTaxAmount = totalCentralTaxAmount.setScale(2, RoundingMode.DOWN);
        this.totalStateTaxAmount = totalStateTaxAmount.setScale(2, RoundingMode.DOWN);
        this.totalTaxAmount = totalTaxAmount.setScale(2, RoundingMode.DOWN);
    }

    @Override
    public String getPrintableHsn() {
        return column0;
    }

    @Override
    public String getPrintableTaxableAmount() {
        return String.valueOf(taxableAmount);
    }

    @Override
    public String getPrintableCentralTaxRate() {
        return "";
    }

    @Override
    public String getPrintableCentralTaxAmount() {
        return String.valueOf(totalCentralTaxAmount);
    }

    @Override
    public String getPrintableStateTaxRate() {
        return "";
    }

    @Override
    public String getPrintableStateTaxAmount() {
        return String.valueOf(totalStateTaxAmount);
    }

    @Override
    public String getPrintableTotalTaxAmount() {
        return String.valueOf(totalTaxAmount);
    }
}
