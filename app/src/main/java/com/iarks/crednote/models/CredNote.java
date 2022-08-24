package com.iarks.crednote.models;

import android.util.Pair;

import com.iarks.crednote.service.CurrencyUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CredNote {



    public final Organisation organisation;
    public final Organisation party;
    public final InvoiceDetail invoiceDetails;
    private List<Good> goods;

    private BigDecimal sgst;
    private BigDecimal cgst;

    public HashMap<String, HsnDetails> uniqueHsnAmounts;

    public CredNote(Organisation organisation, Organisation party, InvoiceDetail invoiceDetails) {
        this.organisation = organisation;
        this.party = party;
        this.invoiceDetails = invoiceDetails;
        this.goods = new ArrayList<Good>();
        this.uniqueHsnAmounts = new HashMap<>();
    }

    public BigDecimal getSgst() {
        return sgst;
    }

    public void setSgst(BigDecimal sgst) {
        this.sgst = sgst;
    }

    public BigDecimal getCgst() {
        return cgst;
    }

    public void setCgst(BigDecimal cgst) {
        this.cgst = cgst;
    }

    public void addGoods(Good good)
    {
        this.goods.add(good);
        String hsnCode = good.getHsnCode();
        if(uniqueHsnAmounts.containsKey(hsnCode) == false)
        {
            uniqueHsnAmounts.put(hsnCode, new HsnDetails(hsnCode));
        }
    }

    public Pair<Float, String> getTotal()
    {
        BigDecimal total = BigDecimal.ZERO;

        for (Good good: goods)
        {
            total.add(good.getAmount());
        }
        return new Pair(total, CurrencyUtil.getMoneyInWords(total));
    }

    public BigDecimal getTotalCentralTaxValue()
    {
        BigDecimal result = BigDecimal.ZERO;
        for(Map.Entry<String, HsnDetails> mapEntry:uniqueHsnAmounts.entrySet())
        {
            result.add(mapEntry.getValue().getCentralTaxAmount()).setScale(2);
        }
        return result;
    }

    public BigDecimal getTotalStateTaxValue()
    {
        BigDecimal result = BigDecimal.ZERO.setScale(2);
        for(Map.Entry<String, HsnDetails> mapEntry:uniqueHsnAmounts.entrySet())
        {
            result.add(mapEntry.getValue().getStateTaxAmount());
        }
        return result;
    }

    public BigDecimal getTotalTaxValue()
    {
        BigDecimal result = BigDecimal.ZERO.setScale(2);
        for(Map.Entry<String, HsnDetails> mapEntry:uniqueHsnAmounts.entrySet())
        {
            result.add(mapEntry.getValue().getTotalTaxAmount());
        }
        return result;
    }

    public String getOrganizationName()
    {
        return organisation.getName();
    }

    public int getNumberOfGoods(){
        return goods.size();
    }

    public Good getGood(int id)
    {
        if(id>goods.size())
        {
            return null;
        }

        return goods.get(id);
    }

    public float getRoundOff() {
        return 0.0f;
    }
}
