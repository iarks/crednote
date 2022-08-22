package com.iarks.crednote.models;

import java.util.Date;
import java.util.HashMap;

public class InvoiceDetail {
    private String invoiceNumber;
    private Date date;
    private String deliveryNode;
    private String modeOfPayment;
    private String suppliersReference;
    private String otherReferences;
    private String buyersOrderNumber;
    private Date dated;
    private String dispatchDocumentNumber;
    private String dispatchedThrough;
    private Date deliveryDateNote;
    private String destination;
    private String termsOfDelivery;

    public InvoiceDetail(String invoiceNumber, Date date, String deliveryNode, String modeOfPayment, String suppliersReference, String otherReferences, String buyersOrderNumber, Date dated, String dispatchDocumentNumber, Date deliveryDateNote, String dispatchedThrough, String destination, String termsOfDelivery) {
        this.invoiceNumber = invoiceNumber;
        this.date = date;
        this.deliveryNode = deliveryNode;
        this.modeOfPayment = modeOfPayment;
        this.suppliersReference = suppliersReference;
        this.otherReferences = otherReferences;
        this.buyersOrderNumber = buyersOrderNumber;
        this.dated = dated;
        this.dispatchDocumentNumber = dispatchDocumentNumber;
        this.deliveryDateNote = deliveryDateNote;
        this.dispatchedThrough = dispatchedThrough;
        this.destination = destination;
        this.termsOfDelivery = termsOfDelivery;
        mappings = new String[]{ invoiceNumber, date.toString(), deliveryNode, modeOfPayment, suppliersReference, otherReferences, buyersOrderNumber, dated.toString(), dispatchDocumentNumber, deliveryDateNote.toString(), dispatchedThrough, destination, termsOfDelivery };
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
