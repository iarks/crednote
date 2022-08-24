package com.iarks.crednote.presentation.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.iarks.crednote.Exceptions.FormNotSavedException;
import com.iarks.crednote.R;
import com.iarks.crednote.models.Organisation;
import com.iarks.crednote.presentation.ui.main.NewInvoiceFragmentsAdapter;

import java.time.Duration;

public class OrganizationAndPartyFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private boolean fragmentStateIsEditable = false;
    private TextInputEditText companyName;
    private TextInputEditText address;
    private TextInputEditText gstin;
    private TextInputEditText stateName;
    private TextInputEditText stateCode;
    private Button saveButton;
    private Bundle savedBundle;
    private Context context;
    public final PartyType partyType;
    private NewInvoiceFragmentsAdapter parentAdapter;

    public enum Keys
    {
        company_name,
        address,
        gstin,
        state_name,
        state_code,
        form_state_is_editable,
        context
    }

    public enum PartyType
    {
        Organisation,
        Party
    }

    public OrganizationAndPartyFragment() {
        partyType = PartyType.Organisation;
    }

    public OrganizationAndPartyFragment(PartyType partyType, Context context) {
        this.partyType = partyType;
        this.context = context;
    }

    public void setContext(Context ctx)
    {
        context = ctx;
    }

    // TODO: Rename and change types and number of parameters
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedBundle = savedInstanceState;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_organization_and_party, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentStateIsEditable=true;
        companyName = view.findViewById(R.id.companyName);
        address = view.findViewById(R.id.companyAddress);
        gstin = view.findViewById(R.id.gstinNumber);
        stateName = view.findViewById(R.id.state);
        stateCode = view.findViewById(R.id.stateCode);
        saveButton = view.findViewById(R.id.saveOrg);

        saveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            saveButtonClick();
                        } catch (FormNotSavedException e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG);
                        }
                    }
                }
        );

        if(savedInstanceState!=null)
        {
            if(savedInstanceState.containsKey(Keys.company_name.name()))
            {
                companyName.setText((String)savedInstanceState.get(Keys.company_name.name()));
            }
            if(savedInstanceState.containsKey(Keys.address.name()))
            {
                companyName.setText((String)savedInstanceState.get(Keys.address.name()));
            }
            if(savedInstanceState.containsKey(Keys.gstin.name()))
            {
                companyName.setText((String)savedInstanceState.get(Keys.gstin.name()));
            }
            if(savedInstanceState.containsKey(Keys.state_code.name()))
            {
                companyName.setText((String)savedInstanceState.get(Keys.state_code.name()));
            }
            if(savedInstanceState.containsKey(Keys.state_name.name()))
            {
                companyName.setText((String)savedInstanceState.get(Keys.state_name.name()));
            }
            if(savedInstanceState.containsKey(Keys.form_state_is_editable.name()))
            {
                boolean formStateIsEditable = (boolean)savedInstanceState.get(Keys.form_state_is_editable.name());
                fragmentStateIsEditable = formStateIsEditable;
                if(!formStateIsEditable)
                {
                    toggleAllFields(false, getString(R.string.label_edit));
                }

            }

        }
    }

    public void saveButtonClick() throws FormNotSavedException
    {
        if(fragmentStateIsEditable)
        {
            checkFields();
            fragmentStateIsEditable = false;
            toggleAllFields(false, getString(R.string.label_edit));
        }
        else
        {
            fragmentStateIsEditable = true;
            toggleAllFields(false, getString(R.string.label_save));
        }
    }

    private void checkFields() throws FormNotSavedException {
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

    private void toggleAllFields(boolean isEnabled, String buttonText) {
        companyName.setEnabled(false);
        address.setEnabled(false);
        gstin.setEnabled(false);
        stateName.setEnabled(false);
        stateCode.setEnabled(false);
        saveButton.setText(buttonText);
    }

    public Organisation getOrgDetails() throws FormNotSavedException
    {
        if(fragmentStateIsEditable)
        {
            throw new FormNotSavedException("Please save organization form");
        }
        return new Organisation(companyName.getText().toString(),
                address.getText().toString().split("\n"),
                gstin.getText().toString(),
                stateName.getText().toString(),
                Integer.parseInt(stateCode.getText().toString()));
    }
}