package com.iarks.crednote.abstractions;

import com.iarks.crednote.Exceptions.FormNotSavedException;
import com.iarks.crednote.models.InvoiceDetail;

public interface InvoiceDetailsCarrier {
    InvoiceDetail getInvoiceDetail() throws FormNotSavedException;
}
