package com.iarks.crednote.presentation.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.iarks.crednote.abstractions.InvoiceDetailsCarrier;
import com.iarks.crednote.models.InvoiceDetail;
import com.iarks.crednote.models.Organisation;
import com.iarks.crednote.service.DateTimeUtil;

import java.util.Date;

public class InvoiceDetailsFragment extends Fragment implements InvoiceDetailsCarrier {
    boolean isFragmentFormEditable;

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
    private boolean isFragmentStateSaved;
    private InvoiceDetail invoiceDetail;
    private static String FORM_VALUES = "FORM_VALUES";
    private static String FORM_EDIT_STATE = "FORM_EDIT_STATE";

    public InvoiceDetailsFragment(Context ctx) {
        isFragmentFormEditable = true;
        this.context = ctx;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray(FORM_VALUES, new String[] {
                invoiceNumber.getEditText().getText().toString(),
                invoiceDate.getEditText().getText().toString(),
                deliveryNote.getEditText().getText().toString(),
                modeOfPayment.getEditText().getText().toString(),
                supplierRef.getEditText().getText().toString(),
                otherRef.getEditText().getText().toString(),
                orderNumber.getEditText().getText().toString(),
                orderNumberDate.getEditText().getText().toString(),
                dispatchDocNumber.getEditText().getText().toString(),
                deliveryNoteDate.getEditText().getText().toString(),
                dispatchedThrough.getEditText().getText().toString(),
                destination.getEditText().getText().toString(),
                termsOfDelivery.getEditText().getText().toString(),
        });

        outState.putBoolean(FORM_EDIT_STATE, isFragmentFormEditable);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_invoice_details, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        extractViews(view);
        attachEventListeners();
        tryRestoreFormState(savedInstanceState);
    }

    private void tryRestoreFormState(Bundle savedInstanceState) {
        if(savedInstanceState!=null)
        {
            if (savedInstanceState.containsKey(FORM_VALUES)) {
                String[] values = savedInstanceState.getStringArray(FORM_VALUES);
                invoiceNumber.getEditText().setText(values[0]);
                invoiceDate.getEditText().setText(values[1]);
                deliveryNote.getEditText().setText(values[2]);
                modeOfPayment.getEditText().setText(values[3]);
                supplierRef.getEditText().setText(values[4]);
                otherRef.getEditText().setText(values[5]);
                orderNumber.getEditText().setText(values[6]);
                orderNumberDate.getEditText().setText(values[7]);
                dispatchDocNumber.getEditText().setText(values[8]);
                deliveryNoteDate.getEditText().setText(values[9]);
                dispatchedThrough.getEditText().setText(values[10]);
                destination.getEditText().setText(values[11]);
                termsOfDelivery.getEditText().setText(values[12]);
            }

            if(savedInstanceState.containsKey(FORM_EDIT_STATE))
            {
                isFragmentFormEditable = savedInstanceState.getBoolean(FORM_EDIT_STATE);
                setFieldEditState();
            }
        }
    }

    private void attachEventListeners() {
        saveButton.setOnClickListener(view1 -> {
            try {
                onSaveButtonClick();
            } catch (FormNotSavedException e) {
                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void extractViews(@NonNull View view) {
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
    }

    private void onSaveButtonClick() throws FormNotSavedException {
        isFragmentFormEditable=!isFragmentFormEditable;
        setFieldEditState();
        if(isFragmentFormEditable==false)
        {
            saveForm();
        }
        else
        {
            unsaveForm();
        }
    }

    private void unsaveForm() {
        isFragmentStateSaved = false;
        invoiceDetail = null;
    }

    private void saveForm() {
        isFragmentStateSaved = true;
        String invNumber = invoiceNumber.getEditText().getText().toString();
        Date invDate = DateTimeUtil.tryParseDate(invoiceDate.getEditText().getText().toString());
        String delNode = deliveryNote.getEditText() == null ? "" : deliveryNote.getEditText().getText().toString();
        String modPay = modeOfPayment.getEditText() == null ? "" : modeOfPayment.getEditText().getText().toString();
        String suppRef = supplierRef.getEditText() == null ? "" : supplierRef.getEditText().getText().toString();
        String otRef = otherRef.getEditText() == null ? "" : otherRef.getEditText().getText().toString();
        String buyOrNo = orderNumber.getEditText() == null ? "" : orderNumber.getEditText().getText().toString();
        Date orNoDate = DateTimeUtil.tryParseDate(orderNumberDate.getEditText().getText().toString());
        String dispN = dispatchDocNumber.getEditText() == null ? "" : dispatchDocNumber.getEditText().getText().toString();
        Date delNoDate = DateTimeUtil.tryParseDate(deliveryNoteDate.getEditText().getText().toString());
        String dispTh = dispatchedThrough.getEditText() == null ? "" : dispatchedThrough.getEditText().getText().toString();
        String dest = destination.getEditText() == null ? "" : destination.getEditText().getText().toString();
        String tod = termsOfDelivery.getEditText() == null ? "" : termsOfDelivery.getEditText().getText().toString();

        invoiceDetail = new InvoiceDetail(invNumber,
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

    private void setFieldEditState() {
        invoiceNumber.setEnabled(isFragmentFormEditable);
        invoiceDate.setEnabled(isFragmentFormEditable);
        deliveryNote.setEnabled(isFragmentFormEditable);
        modeOfPayment.setEnabled(isFragmentFormEditable);
        supplierRef.setEnabled(isFragmentFormEditable);
        otherRef.setEnabled(isFragmentFormEditable);
        orderNumber.setEnabled(isFragmentFormEditable);
        orderNumberDate.setEnabled(isFragmentFormEditable);
        dispatchDocNumber.setEnabled(isFragmentFormEditable);
        deliveryNoteDate.setEnabled(isFragmentFormEditable);
        dispatchedThrough.setEnabled(isFragmentFormEditable);
        destination.setEnabled(isFragmentFormEditable);
        termsOfDelivery.setEnabled(isFragmentFormEditable);
        if(isFragmentFormEditable)
            saveButton.setText(R.string.label_save);
        else
            saveButton.setText(R.string.label_edit);
    }

    @Override
    public InvoiceDetail getInvoiceDetail() throws FormNotSavedException
    {
        if(isFragmentStateSaved)
            return invoiceDetail;
        throw new FormNotSavedException("INVOICE DETAIL " +getString(R.string.FORM_NOT_SAVED));
    }
}