package com.iarks.crednote.service;

import android.text.Editable;
import android.text.TextWatcher;

import com.iarks.crednote.models.Good;
import com.iarks.crednote.models.HsnDetails;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class HsnFormUtil {
    public static class TaxableAmountChangeListener implements TextWatcher
    {
        private HsnDetails hsnDetail;

        public void updatePosition(HsnDetails hsnDetail) {
            this.hsnDetail = hsnDetail;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            BigDecimal taxableAmount = charSequence.length() == 0 || charSequence.toString().trim().equals("") ? new BigDecimal("0.00") : new BigDecimal(charSequence.toString());
            hsnDetail.setTaxableAmount(taxableAmount);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    public static class CentralTaxRateChangeListener implements TextWatcher
    {
        private HsnDetails hsnDetail;

        public void updatePosition(HsnDetails hsnDetail) {
            this.hsnDetail = hsnDetail;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            BigDecimal taxableAmount = charSequence.length() == 0 || charSequence.toString().trim().equals("") ? new BigDecimal("0.00") : new BigDecimal(charSequence.toString());
            hsnDetail.setCentralTaxRate(taxableAmount);
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    public static class HsnCodeChangeListener implements  TextWatcher
    {
        private HsnDetails hsnDetail;

        public void updatePosition(HsnDetails hsnDetail) {
            this.hsnDetail = hsnDetail;
        }


        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            String hsnCode = charSequence.toString();
            hsnDetail.setHsnCode(hsnCode);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    public static class StateTaxRateChangeListener implements TextWatcher
    {
        private HsnDetails hsnDetail;

        public void updatePosition(HsnDetails hsnDetail) {
            this.hsnDetail = hsnDetail;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            BigDecimal taxableAmount = charSequence.length() == 0 || charSequence.toString().trim().equals("") ? new BigDecimal("0.00") : new BigDecimal(charSequence.toString());
            hsnDetail.setStateTaxRate(taxableAmount);
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }
}
