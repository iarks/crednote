package com.iarks.crednote.presentation.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textfield.TextInputLayout;
import com.iarks.crednote.R;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TaxFragment extends Fragment {

    private TextInputLayout cgst;
    private TextInputLayout sgst;
    private TextInputLayout roundOff;
    private TextInputLayout total;
    private TextInputLayout taxableAmount;
    private TextInputLayout totalCentralTax;
    private TextInputLayout totalStateTax;
    private TextInputLayout totalTax;
    private Button save;
    private Button evalTop;
    private Button evalBottom;

    private boolean isFormEditable = true;

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray("fieldValues", new String[] {
                cgst.getEditText().getText().toString(),
                sgst.getEditText().getText().toString(),
                roundOff.getEditText().getText().toString(),
                total.getEditText().getText().toString(),
                taxableAmount.getEditText().getText().toString(),
                totalCentralTax.getEditText().getText().toString(),
                totalStateTax.getEditText().getText().toString(),
                totalTax.getEditText().getText().toString()
        });
        outState.putBoolean("isFormEditable", isFormEditable);

        System.out.println("on save instance state called");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        System.out.println("on create called");
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
        evalBottom = v.findViewById(R.id.evaluateTaxBottom);

        save.setOnClickListener(view -> {
            isFormEditable=!isFormEditable;
            ToggleForm(isFormEditable);
        });

        evalTop.setOnClickListener(view -> {
            BigDecimal cgstValue = cgst.getEditText().getText().toString().trim().equals("") ? new BigDecimal("0.00") : new BigDecimal(cgst.getEditText().getText().toString());
            BigDecimal sgstValue = sgst.getEditText().getText().toString().trim().equals("") ? new BigDecimal("0.00") : new BigDecimal(sgst.getEditText().getText().toString());
            BigDecimal roundOffValue = roundOff.getEditText().getText().toString().trim().equals("") ? new BigDecimal("0.00") : new BigDecimal(roundOff.getEditText().getText().toString());

            BigDecimal eval = cgstValue.add(sgstValue).setScale(2, RoundingMode.DOWN).add(roundOffValue).setScale(2, RoundingMode.DOWN);
            total.getEditText().setText(String.valueOf(eval));
        });

        evalBottom.setOnClickListener(view -> {
            BigDecimal taxableAmountVal = taxableAmount.getEditText().getText().toString().trim().equals("") ? new BigDecimal("0.00") : new BigDecimal(taxableAmount.getEditText().getText().toString());
            BigDecimal totalCentralTaxVal = totalCentralTax.getEditText().getText().toString().trim().equals("") ? new BigDecimal("0.00") : new BigDecimal(totalCentralTax.getEditText().getText().toString());
            BigDecimal totalStateTaxVal = totalStateTax.getEditText().getText().toString().trim().equals("") ? new BigDecimal("0.00") : new BigDecimal(totalStateTax.getEditText().getText().toString());

            BigDecimal eval = taxableAmountVal.add(totalCentralTaxVal).setScale(2, RoundingMode.DOWN).add(totalStateTaxVal).setScale(2, RoundingMode.DOWN);
            totalTax.getEditText().setText(String.valueOf(eval));
        });

        if(savedInstanceState==null)
            return v;

        if(savedInstanceState.containsKey("fieldValues"))
        {
            String[] savedInstance = savedInstanceState.getStringArray("fieldValues");
            cgst.getEditText().setText(savedInstance[0]);
            sgst.getEditText().setText(savedInstance[1]);
            roundOff.getEditText().setText(savedInstance[2]);
            total.getEditText().setText(savedInstance[3]);
            taxableAmount.getEditText().setText(savedInstance[4]);
            totalCentralTax.getEditText().setText(savedInstance[5]);
            totalStateTax.getEditText().setText(savedInstance[6]);
            totalTax.getEditText().setText(savedInstance[7]);
        }

        if(savedInstanceState.containsKey("isFormEditable"))
        {
            isFormEditable = savedInstanceState.getBoolean("isFormEditable");
            ToggleForm(isFormEditable);
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
        evalBottom.setEnabled(isFormEditable);
        evalTop.setEnabled(isFormEditable);
        if(isFormEditable)
            save.setText(R.string.label_save);
        else
            save.setText(R.string.label_edit);
    }


}