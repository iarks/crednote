package com.iarks.crednote.models;

import com.iarks.crednote.service.DateTimeUtil;

import java.util.Date;
import java.util.HashMap;

public class InvoiceDetail {
    private String termsOfDelivery;

    public InvoiceDetail(String invoiceNumber, Date date, String deliveryNode, String modeOfPayment, String suppliersReference, String otherReferences, String buyersOrderNumber, Date dated, String dispatchDocumentNumber, Date deliveryDateNote, String dispatchedThrough, String destination, String termsOfDelivery) {
        this.termsOfDelivery = termsOfDelivery;
        mappings = new String[]{
                invoiceNumber,
                date.equals(DateTimeUtil.defaultDate) ? "" : DateTimeUtil.defaultFormat.format(date),
                deliveryNode,
                modeOfPayment,
                suppliersReference,
                otherReferences,
                buyersOrderNumber,
                dated.equals(DateTimeUtil.defaultDate) ? "" : DateTimeUtil.defaultFormat.format(dated),
                dispatchDocumentNumber,
                deliveryDateNote.equals(DateTimeUtil.defaultDate) ? "" : DateTimeUtil.defaultFormat.format(deliveryDateNote),
                dispatchedThrough,
                destination,
                termsOfDelivery };
    }

    public InvoiceDetail()
    {
        mappings = new String[13];
    }

    public String getById(int id)
    {
        if(id>=mappings.length)
            return "";
        return mappings[id];
    }

    private String[] mappings;

    public String getTermsOfDelivery() {
        return termsOfDelivery;
    }
}
