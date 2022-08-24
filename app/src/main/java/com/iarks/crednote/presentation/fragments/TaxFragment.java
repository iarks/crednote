package com.iarks.crednote.presentation.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.iarks.crednote.Exceptions.FormNotSavedException;
import com.iarks.crednote.R;
import com.iarks.crednote.abstractions.TaxDetailsCarrier;
import com.iarks.crednote.models.GoodLineItem;
import com.iarks.crednote.models.GoodsAmountCarrier;
import com.iarks.crednote.models.TaxAmountCarrier;
import com.iarks.crednote.models.TaxDetails;
import com.iarks.crednote.models.TaxLine;
import com.iarks.crednote.models.TaxLineItem;
import com.iarks.crednote.service.CurrencyUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TaxFragment extends Fragment implements TaxDetailsCarrier {

    private TextInputLayout goodsTotal;
    private TextInputLayout cgst;
    private TextInputLayout sgst;
    private TextInputLayout roundOff;
    private TextInputLayout total;
    private TextInputLayout taxableAmount;
    private TextInputLayout totalCentralTax;
    private TextInputLayout totalStateTax;
    private TextInputLayout totalTax;
    private TextInputLayout totalInWords;
    private Button save;
    private Button evalTop;
    private Button goodTotalsButton;
    private Button getHsnValues;

    private TaxDetails taxDetails;

    private boolean isFragmentFormEditable = true;

    private TaxAmountCarrier taxAmountCarrier;
    private GoodsAmountCarrier goodsAmountCarrier;

    public TaxFragment(TaxAmountCarrier taxAmountCarrier, GoodsAmountCarrier goodsAmountCarrier) {
        this.taxAmountCarrier = taxAmountCarrier;
        this.goodsAmountCarrier = goodsAmountCarrier;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray("fieldValues", new String[] {
                goodsTotal.getEditText().getText().toString(),
                cgst.getEditText().getText().toString(),
                sgst.getEditText().getText().toString(),
                roundOff.getEditText().getText().toString(),
                total.getEditText().getText().toString(),
                totalInWords.getEditText().getText().toString(),
                taxableAmount.getEditText().getText().toString(),
                totalCentralTax.getEditText().getText().toString(),
                totalStateTax.getEditText().getText().toString(),
                totalTax.getEditText().getText().toString()
        });
        outState.putBoolean("isFormEditable", isFragmentFormEditable);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tax, container, false);

        cgst = v.findViewById(R.id.cgst);
        sgst = v.findViewById(R.id.sgst);
        roundOff = v.findViewById(R.id.roundOff);
        total = v.findViewById(R.id.total);
        taxableAmount = v.findViewById(R.id.taxableAmount);
        totalCentralTax = v.findViewById(R.id.totalCentralTaxAmount);
        totalStateTax = v.findViewById(R.id.totalStateTaxAmount);
        totalTax = v.findViewById(R.id.totalTaxAmount);
        save = v.findViewById(R.id.saveOrg);
        evalTop = v.findViewById(R.id.evaluateTaxTop);
        goodTotalsButton = v.findViewById(R.id.getGoodsTotals);
        getHsnValues = v.findViewById(R.id.getHsnValues);
        totalInWords = v.findViewById(R.id.totalInWords);
        goodsTotal = v.findViewById(R.id.totalGoods);

        save.setOnClickListener(view -> {
            isFragmentFormEditable =!isFragmentFormEditable;
            ToggleForm(isFragmentFormEditable);
            if(isFragmentFormEditable == false)
            {
                BigDecimal goodsTotalAmt = goodsTotal.getEditText().getText().toString().trim().equals("") ? new BigDecimal("0.00") : new BigDecimal(goodsTotal.getEditText().getText().toString()).setScale(2, RoundingMode.DOWN);
                BigDecimal cgstAmt = cgst.getEditText().getText().toString().trim().equals("") ? new BigDecimal("0.00") : new BigDecimal(cgst.getEditText().getText().toString()).setScale(2, RoundingMode.DOWN);
                BigDecimal sgstAmt = sgst.getEditText().getText().toString().trim().equals("") ? new BigDecimal("0.00") : new BigDecimal(sgst.getEditText().getText().toString()).setScale(2, RoundingMode.DOWN);
                BigDecimal roundOffAmt = roundOff.getEditText().getText().toString().trim().equals("") ? new BigDecimal("0.00") : new BigDecimal(roundOff.getEditText().getText().toString()).setScale(2, RoundingMode.DOWN);
                BigDecimal totalAmt = total.getEditText().getText().toString().trim().equals("") ? new BigDecimal("0.00") : new BigDecimal(total.getEditText().getText().toString()).setScale(2, RoundingMode.DOWN);
                String totalWords = totalInWords.getEditText().getText().toString();

                BigDecimal taxableAmt = taxableAmount.getEditText().getText().toString().trim().equals("") ? new BigDecimal("0.00") : new BigDecimal(taxableAmount.getEditText().getText().toString()).setScale(2, RoundingMode.DOWN);
                BigDecimal centralTaxAmt = totalCentralTax.getEditText().getText().toString().trim().equals("") ? new BigDecimal("0.00") : new BigDecimal(totalCentralTax.getEditText().getText().toString()).setScale(2, RoundingMode.DOWN);
                BigDecimal totalStateTaxAmount = totalStateTax.getEditText().getText().toString().trim().equals("") ? new BigDecimal("0.00") : new BigDecimal(totalStateTax.getEditText().getText().toString()).setScale(2, RoundingMode.DOWN);
                BigDecimal totalTaxAmount = totalTax.getEditText().getText().toString().trim().equals("") ? new BigDecimal("0.00") : new BigDecimal(totalTax.getEditText().getText().toString()).setScale(2, RoundingMode.DOWN);

                taxDetails = new TaxDetails(goodsTotalAmt, cgstAmt, sgstAmt, roundOffAmt, totalAmt, taxableAmt, centralTaxAmt, totalStateTaxAmount, totalTaxAmount);
                taxDetails.setTotalInWords(totalWords);
            }
            else
            {
                taxDetails = null;
            }
        });

        evalTop.setOnClickListener(view -> {
            BigDecimal goodsAmount = goodsTotal.getEditText().getText().toString().trim().equals("") ? new BigDecimal("0.00") : new BigDecimal(goodsTotal.getEditText().getText().toString()).setScale(2, RoundingMode.DOWN);
            BigDecimal cgstValue = cgst.getEditText().getText().toString().trim().equals("") ? new BigDecimal("0.00") : new BigDecimal(cgst.getEditText().getText().toString()).setScale(2, RoundingMode.DOWN);
            BigDecimal sgstValue = sgst.getEditText().getText().toString().trim().equals("") ? new BigDecimal("0.00") : new BigDecimal(sgst.getEditText().getText().toString()).setScale(2, RoundingMode.DOWN);
            BigDecimal roundOffValue = roundOff.getEditText().getText().toString().trim().equals("") ? new BigDecimal("0.00") : new BigDecimal(roundOff.getEditText().getText().toString()).setScale(2, RoundingMode.DOWN);

            BigDecimal eval = goodsAmount.add(cgstValue).add(sgstValue).add(roundOffValue).setScale(2, RoundingMode.DOWN);
            total.getEditText().setText(String.valueOf(eval));
            totalInWords.getEditText().setText(CurrencyUtil.getMoneyInWords(eval));
        });

        goodTotalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BigDecimal goodsAmount = goodsAmountCarrier.getGoodsTotal();

                goodsTotal.getEditText().setText(String.valueOf(goodsAmount));
            }
        });

        getHsnValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BigDecimal taxableAmountVal = taxAmountCarrier.getTotalTaxableAmount();
                BigDecimal totalCentralTaxVal = taxAmountCarrier.getTotalCentralTaxAmount();
                BigDecimal totalStateTaxVal = taxAmountCarrier.getTotalStateTaxAmount();
                BigDecimal totalTaxVal = taxAmountCarrier.getTotalTaxAmount();

                taxableAmount.getEditText().setText(String.valueOf(taxableAmountVal));
                totalCentralTax.getEditText().setText(String.valueOf(totalCentralTaxVal));
                totalStateTax.getEditText().setText(String.valueOf(totalStateTaxVal));
                totalTax.getEditText().setText(String.valueOf(totalTaxVal));

            }
        });



        if(savedInstanceState==null)
            return v;

        if(savedInstanceState.containsKey("fieldValues"))
        {
            String[] savedInstance = savedInstanceState.getStringArray("fieldValues");
            goodsTotal.getEditText().setText(savedInstance[0]);
            cgst.getEditText().setText(savedInstance[1]);
            sgst.getEditText().setText(savedInstance[2]);
            roundOff.getEditText().setText(savedInstance[3]);
            total.getEditText().setText(savedInstance[4]);
            totalInWords.getEditText().setText(savedInstance[5]);
            taxableAmount.getEditText().setText(savedInstance[6]);
            totalCentralTax.getEditText().setText(savedInstance[7]);
            totalStateTax.getEditText().setText(savedInstance[8]);
            totalTax.getEditText().setText(savedInstance[9]);
        }

        if(savedInstanceState.containsKey("isFormEditable"))
        {
            isFragmentFormEditable = savedInstanceState.getBoolean("isFormEditable");
            ToggleForm(isFragmentFormEditable);
        }

        return v;
    }

    private void ToggleForm(boolean isFormEditable) {
        cgst.setEnabled(isFormEditable);
        sgst.setEnabled(isFormEditable);
        roundOff.setEnabled(isFormEditable);
        total.setEnabled(isFormEditable);
        taxableAmount.setEnabled(isFormEditable);
        totalCentralTax.setEnabled(isFormEditable);
        totalStateTax.setEnabled(isFormEditable);
        totalTax.setEnabled(isFormEditable);
        totalInWords.setEnabled(isFormEditable);
        goodTotalsButton.setEnabled(isFormEditable);
        getHsnValues.setEnabled(isFormEditable);
        evalTop.setEnabled(isFormEditable);
        if(isFormEditable)
            save.setText(R.string.label_save);
        else
            save.setText(R.string.label_edit);
    }

    @Override
    public TaxDetails getTaxDetails() throws FormNotSavedException
    {
        if(taxDetails == null)
            throw new FormNotSavedException("Tax Details "+getString(R.string.FORM_NOT_SAVED));

        return taxDetails;
    }


}