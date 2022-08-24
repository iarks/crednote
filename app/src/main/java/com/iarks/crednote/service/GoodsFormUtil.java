package com.iarks.crednote.service;

import android.text.Editable;
import android.text.TextWatcher;

import com.iarks.crednote.models.Good;
import com.iarks.crednote.presentation.fragments.GoodsRecyclerViewAdapter;

import java.math.BigDecimal;
import java.util.List;

public class GoodsFormUtil {
    public static class GoodsDescriptionChangeListener implements TextWatcher
    {
        private Good good;

        public void updatePosition(Good good) {
            this.good = good;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            good.setDescription(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    public static class GoodsCodeChangeListener implements TextWatcher
    {
        private Good good;

        public void updatePosition(Good good) {
            this.good = good;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            int tryParse =0;
            try{
            tryParse = Integer.parseInt(charSequence.toString());
            }catch (Exception ignored)
            {

            }
            good.setHsn(tryParse);
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }

    public static class GoodsQuantityChangeListener implements TextWatcher
    {
        private Good good;

        public void updatePosition(Good good) {
            this.good = good;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            BigDecimal quantity = charSequence.length() == 0 || charSequence.toString().trim().equals("") ? new BigDecimal("0.00") : new BigDecimal(charSequence.toString());
            good.setQuantity(quantity);

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    public static class GoodsRateChangeListener implements TextWatcher
    {
        Good good;

        public void updatePosition(Good good) {
            this.good = good;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            BigDecimal rate = charSequence.length() == 0 || charSequence.toString().trim().equals("") ? new BigDecimal("0.00") : new BigDecimal(charSequence.toString());
            good.setRate(rate);

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    public static class GoodsUnitChangeListener implements TextWatcher
    {
        private Good good;

        public void updatePosition(Good good) {
            this.good = good;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            good.setUnit(charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    public static class GoodsDiscountChangeListener implements TextWatcher
    {
        private Good good;

        public void updatePosition(Good good) {
            this.good = good;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            BigDecimal discount = charSequence.length() == 0 || charSequence.toString().trim().equals("") ? new BigDecimal("0.00") : new BigDecimal(charSequence.toString());
            good.setDiscountPercentage(discount);
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }

    public static class GoodsAmountChangeListener implements TextWatcher
    {
        private Good good;

        public void updatePosition(Good good) {
            this.good = good;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            BigDecimal amount = charSequence.length() == 0 || charSequence.toString().trim().equals("") ? new BigDecimal("0.00") : new BigDecimal(charSequence.toString());
            good.setAmount(amount);

        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }
}
