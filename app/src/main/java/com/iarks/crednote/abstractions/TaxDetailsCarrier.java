package com.iarks.crednote.abstractions;

import com.iarks.crednote.Exceptions.FormNotSavedException;
import com.iarks.crednote.models.TaxDetails;

public interface TaxDetailsCarrier {
    TaxDetails getTaxDetails() throws FormNotSavedException;
}
