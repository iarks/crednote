package com.iarks.crednote.models;

import java.math.BigDecimal;

public interface TaxAmountCarrier {
    BigDecimal getTotalTaxableAmount();
    BigDecimal getTotalCentralTaxAmount();
    BigDecimal getTotalStateTaxAmount();
    BigDecimal getTotalTaxAmount();
}
