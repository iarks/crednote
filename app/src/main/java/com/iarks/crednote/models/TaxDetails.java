package com.iarks.crednote.models;

import com.google.android.material.textfield.TextInputLayout;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class TaxDetails {

    public String getTotalInWords() {
        return totalInWords;
    }

    private String totalInWords;

    public TaxDetails() {
        this.goodsTotal = new BigDecimal("0.00").setScale(2, RoundingMode.DOWN);
        this.cgst = new BigDecimal("0.00").setScale(2, RoundingMode.DOWN);
        this.sgst = new BigDecimal("0.00").setScale(2, RoundingMode.DOWN);
        this.roundOff = new BigDecimal("0.00").setScale(2, RoundingMode.DOWN);
        this.total = new BigDecimal("0.00").setScale(2, RoundingMode.DOWN);
        this.totalTaxableAmount = new BigDecimal("0.00").setScale(2, RoundingMode.DOWN);
        this.totalCentralTaxAmount = new BigDecimal("0.00").setScale(2, RoundingMode.DOWN);
        this.totalStateTaxAmount = new BigDecimal("0.00").setScale(2, RoundingMode.DOWN);
        this.totalTaxAmount = new BigDecimal("0.00").setScale(2, RoundingMode.DOWN);
    }

    private BigDecimal goodsTotal;
    private BigDecimal cgst;
    private BigDecimal sgst;
    private BigDecimal roundOff;
    private BigDecimal total;

    private BigDecimal totalTaxableAmount;
    private BigDecimal totalCentralTaxAmount;
    private BigDecimal totalStateTaxAmount;
    private BigDecimal totalTaxAmount;

    public TaxDetails(BigDecimal goodsTotal, BigDecimal cgst, BigDecimal sgst, BigDecimal roundOff, BigDecimal grossTotal, BigDecimal totalTaxableAmount, BigDecimal totalCentralTaxAmount, BigDecimal totalStateTaxAmount, BigDecimal totalTaxAmount) {
        this.goodsTotal = goodsTotal;
        this.cgst = cgst;
        this.sgst = sgst;
        this.roundOff = roundOff;
        this.total = grossTotal;
        this.totalTaxableAmount = totalTaxableAmount;
        this.totalCentralTaxAmount = totalCentralTaxAmount;
        this.totalStateTaxAmount = totalStateTaxAmount;
        this.totalTaxAmount = totalTaxAmount;
    }

    public BigDecimal getGoodsTotal() {
        return goodsTotal.setScale(2, RoundingMode.DOWN);
    }

    public void setGoodsTotal(BigDecimal goodsTotal) {
        this.goodsTotal = goodsTotal.setScale(2, RoundingMode.DOWN);;
    }

    public BigDecimal getCgst() {
        return cgst.setScale(2, RoundingMode.DOWN);
    }

    public void setCgst(BigDecimal cgst) {
        this.cgst = cgst.setScale(2, RoundingMode.DOWN);
    }

    public BigDecimal getSgst() {
        return sgst.setScale(2, RoundingMode.DOWN);
    }

    public void setSgst(BigDecimal sgst) {
        this.sgst = sgst.setScale(2, RoundingMode.DOWN);
    }

    public BigDecimal getRoundOff() {
        return roundOff;
    }

    public void setRoundOff(BigDecimal roundOff) {
        this.roundOff = roundOff;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getTotalTaxableAmount() {
        return totalTaxableAmount;
    }

    public void setTotalTaxableAmount(BigDecimal totalTaxableAmount) {
        this.totalTaxableAmount = totalTaxableAmount;
    }

    public BigDecimal getTotalCentralTaxAmount() {
        return totalCentralTaxAmount;
    }

    public void setTotalCentralTaxAmount(BigDecimal totalCentralTaxAmount) {
        this.totalCentralTaxAmount = totalCentralTaxAmount;
    }

    public BigDecimal getTotalStateTaxAmount() {
        return totalStateTaxAmount;
    }

    public void setTotalStateTaxAmount(BigDecimal totalStateTaxAmount) {
        this.totalStateTaxAmount = totalStateTaxAmount;
    }

    public BigDecimal getTotalTaxAmount() {
        return totalTaxAmount;
    }

    public void setTotalTaxAmount(BigDecimal totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    // region utility
    private boolean isLocked = false;

    public void lock()
    {
        isLocked = true;
    }

    public void unlock()
    {
        isLocked = false;
    }

    public boolean isLocked()
    {
        return isLocked;
    }
    // endregion

    // region For document
    public List<GoodLineItem> getGoodsLineItem()
    {
            List<GoodLineItem> goodLineItems = new ArrayList<>(5);
            GoodLineItem goodsTotalItem = new GoodLineItem(CredNote.TOTAL_GOODS_AMOUNT, "Total \u20B9", goodsTotal);
            goodLineItems.add(goodsTotalItem);

            GoodLineItem cgstItem = new GoodLineItem(CredNote.CGST, "CGST \u20B9", cgst);
            goodLineItems.add(cgstItem);

            GoodLineItem sgstItem = new GoodLineItem(CredNote.SGST,"SGST \u20B9", sgst);
            goodLineItems.add(sgstItem);

            GoodLineItem roundOffItem = new GoodLineItem(CredNote.ROUND_OFF, "Round Off \u20B9", roundOff);
            goodLineItems.add(roundOffItem);

            GoodLineItem amountItem = new GoodLineItem(CredNote.GROSS_TOTAL, "Gross Total \u20B9", total);
            goodLineItems.add(amountItem);

            return goodLineItems;

    }

    public List<TaxLineItem> getTaxTotals()
    {
            List<TaxLineItem> taxLineItems = new ArrayList<>(1);
            taxLineItems.add(new TaxLineItem(CredNote.TOTAL_TAX, "Total \u20B9", totalTaxableAmount, totalCentralTaxAmount, totalStateTaxAmount, totalTaxAmount));
            return taxLineItems;
    }

    public void setTotalInWords(String totalInWords) {
        this.totalInWords = totalInWords;
    }
    // endregion
}
