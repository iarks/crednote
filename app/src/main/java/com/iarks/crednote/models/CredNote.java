package com.iarks.crednote.models;

import android.util.Pair;

import com.iarks.crednote.service.CurrencyUtil;

import java.util.ArrayList;
import java.util.List;

public class CredNote {
    public final Organisation organisation;
    public final Organisation party;
    public final InvoiceDetail invoiceDetails;
    private List<Good> goods;

    private double sgst;
    private double cgst;

    public CredNote(Organisation organisation, Organisation party, InvoiceDetail invoiceDetails) {
        this.organisation = organisation;
        this.party = party;
        this.invoiceDetails = invoiceDetails;
        this.goods = new ArrayList<Good>();
    }

    public double getSgst() {
        return sgst;
    }

    public void setSgst(double sgst) {
        this.sgst = sgst;
    }

    public double getCgst() {
        return cgst;
    }

    public void setCgst(double cgst) {
        this.cgst = cgst;
    }

    public void addGoods(Good good)
    {
        this.goods.add(good);
    }

    public Pair<Float, String> getTotal()
    {
        double total = 0.0d;

        for (Good good: goods)
        {
            total+=good.getAmount();
        }
        return new Pair(total, CurrencyUtil.getMoneyInWords(total));
    }

    public String getOrganizationName()
    {
        return organisation.getName();
    }
}
