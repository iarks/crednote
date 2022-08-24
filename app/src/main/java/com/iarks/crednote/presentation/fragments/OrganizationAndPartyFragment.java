package com.iarks.crednote.presentation.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.iarks.crednote.Exceptions.FormNotSavedException;
import com.iarks.crednote.R;
import com.iarks.crednote.abstractions.OrganizationDetailsCarrier;
import com.iarks.crednote.models.Organisation;

public class OrganizationAndPartyFragment extends Fragment implements OrganizationDetailsCarrier {

    private boolean isFragmentFormEditable = false;
    private TextInputEditText companyName;
    private TextInputEditText address;
    private TextInputEditText gstin;
    private TextInputEditText stateName;
    private TextInputEditText stateCode;
    private Button saveButton;
    private Context context;
    public final PartyType partyType;
    private static String FORM_VALUES = "FORM_VALUES";
    private static String FORM_EDIT_STATE = "FORM_EDIT_STATE";
    private Organisation organisation;
    private boolean isFragmentStateSaved;
    private final Organisation defaultOrg;

    public enum PartyType
    {
        ORGANISATION,
        PARTY
    }

    //region Lifecycle Events
    public OrganizationAndPartyFragment(PartyType partyType, Context context) {
        this.partyType = partyType;
        this.context = context;
        this.defaultOrg = null;
    }

    //region Lifecycle Events
    public OrganizationAndPartyFragment(PartyType partyType, Context context, Organisation defaultOrg) {
        this.partyType = partyType;
        this.context = context;
        this.defaultOrg = defaultOrg;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArray(FORM_VALUES, new String[]{
                companyName.getText().toString(),
                address.getText().toString(),
                gstin.getText().toString(),
                stateName.getText().toString(),
                stateCode.getText().toString()
        });

        outState.putBoolean(FORM_EDIT_STATE, isFragmentFormEditable);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_organization_and_party, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isFragmentFormEditable =true;
        extractViews(view);
        attachListeners();
        tryRestoreFormState(savedInstanceState);
    }
    // endregion

    //region Utility Forms
    private void extractViews(@NonNull View view) {
        companyName = view.findViewById(R.id.companyName);
        address = view.findViewById(R.id.companyAddress);
        gstin = view.findViewById(R.id.gstinNumber);
        stateName = view.findViewById(R.id.state);
        stateCode = view.findViewById(R.id.stateCode);
        saveButton = view.findViewById(R.id.saveOrg);
    }

    private void attachListeners() {
        saveButton.setOnClickListener(
                view1 -> {
                    try {
                        onClickSaveButton();
                    } catch (FormNotSavedException e) {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    private void tryRestoreFormState(@Nullable Bundle savedInstanceState) {
        if(savedInstanceState !=null)
        {
            if(savedInstanceState.containsKey(FORM_VALUES))
            {
                String[] values = savedInstanceState.getStringArray(FORM_VALUES);
                companyName.setText(values[0]);
                address.setText(values[1]);
                gstin.setText(values[2]);
                stateName.setText(values[3]);
                stateCode.setText(values[4]);
            }

            if(savedInstanceState.containsKey(FORM_EDIT_STATE))
            {
                isFragmentFormEditable = savedInstanceState.getBoolean(FORM_EDIT_STATE);
                setFieldEditState();
            }
        }

        if(savedInstanceState == null && defaultOrg!=null)
        {
            companyName.setText(defaultOrg.getName());
            address.setText(defaultOrg.getAddress());
            gstin.setText(defaultOrg.getGSTIn());
            stateName.setText(defaultOrg.getState());
            stateCode.setText(String.valueOf(defaultOrg.getStateCode()));
        }
    }
    //endregion

    public void onClickSaveButton() throws FormNotSavedException
    {
        if(isFragmentFormEditable)
        {
            checkFormFields();
        }
        isFragmentFormEditable = !isFragmentFormEditable;
        setFieldEditState();

        if(false == isFragmentFormEditable)
        {
            isFragmentStateSaved = true;
            organisation = new Organisation(companyName.getText().toString(),
                    address.getText().toString().split("\n"),
                    gstin.getText().toString(),
                    stateName.getText().toString(),
                    Integer.parseInt(stateCode.getText().toString()));
        }
        else
        {
            isFragmentStateSaved = false;
            organisation = null;
        }
    }

    private void checkFormFields() throws FormNotSavedException {
        boolean formStateIsValid = true;
        if(gstin.getText().length()==0)
        {
            gstin.setError(getString(R.string.label_required));
            formStateIsValid=false;
        }
        if(stateCode.getText().length()==0){
            stateCode.setError(getString(R.string.label_required));
            formStateIsValid=false;
        }
        if(stateName.getText().length()==0){
            stateName.setError(getString(R.string.label_required));
            formStateIsValid=false;
        }
        if(companyName.getText().length()==0){
            companyName.setError(getString(R.string.label_required));
            formStateIsValid=false;
        }
        if(!formStateIsValid)
        {
            throw new FormNotSavedException("One or more fields do not have valid input");
        }
    }

    private void setFieldEditState() {
        companyName.setEnabled(isFragmentFormEditable);
        address.setEnabled(isFragmentFormEditable);
        gstin.setEnabled(isFragmentFormEditable);
        stateName.setEnabled(isFragmentFormEditable);
        stateCode.setEnabled(isFragmentFormEditable);
        if(isFragmentFormEditable)
            saveButton.setText(R.string.label_save);
        else
            saveButton.setText(R.string.label_edit);
    }

    @Override
    public Organisation getOrganisationDetails() throws FormNotSavedException
    {
        if(isFragmentStateSaved)
            return organisation;
        String message = getString(R.string.FORM_NOT_SAVED);
        throw new FormNotSavedException(partyType.name()+" "+message);
    }
}