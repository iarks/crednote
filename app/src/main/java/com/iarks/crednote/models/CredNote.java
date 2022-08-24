package com.iarks.crednote.models;

import android.util.Pair;

import com.iarks.crednote.service.CurrencyUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CredNote {

    public static final String TOTAL_GOODS_AMOUNT = "TOTAL_GOODS_AMOUNT_KEY";
    public static final String CGST = "CGST_KEY";
    public static final String SGST = "SGST_KEY";
    public static final String ROUND_OFF = "ROUND_OFF_KEY";
    public static final String GROSS_TOTAL = "GROSS_TOTAL_KEY";

    public static final String TOTAL_TAXABLE_AMOUNT = "TOTAL_TAXABLE_AMOUNT";
    public static final String TOTAL_CENTRAL_TAX = "TOTAL_CENTRAL_TAX";
    public static final String TOTAL_STATE_TAX = "TOTAL_STATE_TAX";
    public static final String TOTAL_TAX = "TOTAL_TAX";

    public final Organisation organisation;
    public final Organisation party;
    public final InvoiceDetail invoiceDetails;
    private final List<Good> goods;
    private final List<HsnDetails> hsnDetails;
    private final HashMap<String, GoodLineItem> goodLineItems;
    private final HashMap<String, TaxLineItem> taxLineItems;

    private final String grossTotalInWords;

    private static final Good defaultGood = new Good();
    private static final HsnDetails defaultHsn = new HsnDetails();

    public CredNote(Organisation organisation, Organisation party, InvoiceDetail invoiceDetails, String grossTotalInWords) {
        this.organisation = organisation;
        this.party = party;
        this.invoiceDetails = invoiceDetails;
        this.goods = new ArrayList<Good>();
        this.hsnDetails = new ArrayList<>();
        this.goodLineItems = new HashMap<>();
        this.taxLineItems = new HashMap<>();
        this.grossTotalInWords = grossTotalInWords;
    }

    public void addHsn(HsnDetails hsnDetail)
    {
        hsnDetails.add(hsnDetail);
    }

    public void addGoods(Good good)
    {
        this.goods.add(good);
    }

    public int getNumberOfGoods(){
        return goods.size();
    }

    public int getNumberOfHsn(){
        return hsnDetails.size();
    }

    public Good getGood(int position)
    {
        if(position<0 || position>goods.size())
        {
            return defaultGood;
        }
        return goods.get(position);
    }

    public HsnDetails getHsn(int position)
    {
        if(position<0 || position>hsnDetails.size())
        {
            return defaultHsn;
        }
        return hsnDetails.get(position);
    }

    public void setGoodLineItem(String key, GoodLineItem item) {
        goodLineItems.put(key, item);
    }

    public void setTaxLineItem(String key, TaxLineItem item) {
        taxLineItems.put(key, item);
    }

    public TaxLineItem getTaxLineItem(String key) {
        return taxLineItems.get(key);
    }

    public GoodLineItem getGoodLineItem(String key) {
        return goodLineItems.get(key);
    }

    public String getGrossTotalInWords()
    {
        return grossTotalInWords;
    }
}
