package com.iarks.crednote.models;

public abstract class TaxLine {
    abstract String getPrintableHsn();
    abstract String getPrintableTaxableAmount();
    abstract String getPrintableCentralTaxRate();
    abstract String getPrintableCentralTaxAmount();
    abstract String getPrintableStateTaxRate();
    abstract String getPrintableStateTaxAmount();
    abstract String getPrintableTotalTaxAmount();
}
