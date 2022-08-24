package com.iarks.crednote.presentation.fragments;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.iarks.crednote.Exceptions.FormNotSavedException;
import com.iarks.crednote.R;
import com.iarks.crednote.models.InvoiceDetail;
import com.iarks.crednote.service.DateTimeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class InvoiceDetailsFragment extends Fragment {
    boolean isFragmentStateEditable;

    private TextInputLayout invoiceNumber;
    private TextInputLayout invoiceDate;
    private TextInputLayout deliveryNote;
    private TextInputLayout modeOfPayment;
    private TextInputLayout supplierRef;
    private TextInputLayout otherRef;
    private TextInputLayout orderNumber;
    private TextInputLayout orderNumberDate;
    private TextInputLayout dispatchDocNumber;
    private TextInputLayout deliveryNoteDate;
    private TextInputLayout dispatchedThrough;
    private TextInputLayout destination;
    private TextInputLayout termsOfDelivery;
    private Button saveButton;
    private Context context;

    public InvoiceDetailsFragment(Context ctx) {
        isFragmentStateEditable=true;
        this.context = ctx;
    }
        // Required empty public constructor
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFragmentStateEditable=true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invoice_details, container, false);

        invoiceNumber = view.findViewById(R.id.invoiceNumber);
        invoiceDate = view.findViewById(R.id.invoiceDate);
        deliveryNote = view.findViewById(R.id.deliveryNote);
        modeOfPayment = view.findViewById(R.id.modeOfPayment);
        supplierRef = view.findViewById(R.id.supplierRef);
        otherRef = view.findViewById(R.id.otherRef);
        orderNumber = view.findViewById(R.id.orderNumber);
        orderNumberDate = view.findViewById(R.id.orderDate);
        dispatchDocNumber = view.findViewById(R.id.dispathDocNumber);
        deliveryNoteDate = view.findViewById(R.id.deliveryNoteDate);
        dispatchedThrough = view.findViewById(R.id.dispatchThrough);
        destination = view.findViewById(R.id.destination);
        termsOfDelivery = view.findViewById(R.id.termsOfDelivery);
        saveButton = view.findViewById(R.id.saveOrg);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    saveButtonClick();
                } catch (FormNotSavedException e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG);
                }
            }
        });

        return view;
    }

    private void saveButtonClick() throws FormNotSavedException{
        if(isFragmentStateEditable)
        {
            isFragmentStateEditable = false;
            setAllFields(false, getString(R.string.label_edit));
        }
        else
        {
            isFragmentStateEditable = true;
            setAllFields(true, getString(R.string.label_save));
        }
    }

    private void setAllFields(boolean isEditEnabled, String buttonText) {
        invoiceNumber.setEnabled(isEditEnabled);
        invoiceDate.setEnabled(isEditEnabled);
        deliveryNote.setEnabled(isEditEnabled);
        modeOfPayment.setEnabled(isEditEnabled);
        supplierRef.setEnabled(isEditEnabled);
        otherRef.setEnabled(isEditEnabled);
        orderNumber.setEnabled(isEditEnabled);
        orderNumberDate.setEnabled(isEditEnabled);
        dispatchDocNumber.setEnabled(isEditEnabled);
        deliveryNoteDate.setEnabled(isEditEnabled);
        dispatchedThrough.setEnabled(isEditEnabled);
        destination.setEnabled(isEditEnabled);
        termsOfDelivery.setEnabled(isEditEnabled);
        saveButton.setText(buttonText);
    }

    public InvoiceDetail getInvoiceDetail()
    {
        String invNumber = invoiceNumber.getEditText() == null ? "" : invoiceNumber.getEditText().toString();
        Date invDate = DateTimeUtil.tryParseDate(invoiceDate.getEditText() == null ? "" : invoiceDate.getEditText().toString());
        String delNode = deliveryNote.getEditText() == null ? "" : deliveryNote.getEditText().toString();
        String modPay = modeOfPayment.getEditText() == null ? "" : modeOfPayment.getEditText().toString();
        String suppRef = supplierRef.getEditText() == null ? "" : supplierRef.getEditText().toString();
        String otRef = otherRef.getEditText() == null ? "" : otherRef.getEditText().toString();
        String buyOrNo = orderNumber.getEditText() == null ? "" : orderNumber.getEditText().toString();
        Date orNoDate = DateTimeUtil.tryParseDate(orderNumberDate.getEditText() == null ? "": orderNumberDate.getEditText().toString());
        String dispN = dispatchDocNumber.getEditText() == null ? "" : dispatchDocNumber.getEditText().toString();
        Date delNoDate = DateTimeUtil.tryParseDate(deliveryNoteDate.getEditText() == null ? "": deliveryNoteDate.getEditText().toString());
        String dispTh = dispatchedThrough.getEditText() == null ? "" : dispatchedThrough.getEditText().toString();
        String dest = destination.getEditText() == null ? "" : destination.getEditText().toString();
        String tod = termsOfDelivery.getEditText() == null ? "" : termsOfDelivery.getEditText().toString();

        return new InvoiceDetail(invNumber,
                invDate, delNode, modPay,
                suppRef,
                otRef,
                buyOrNo,
                orNoDate,
                dispN,
                delNoDate,
                dispTh,
                dest,
                tod);
    }


}