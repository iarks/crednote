package com.iarks.crednote.presentation.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.iarks.crednote.R;
import com.iarks.crednote.abstractions.HsnDetailsCarrier;
import com.iarks.crednote.models.HsnDetails;
import com.iarks.crednote.models.TaxAmountCarrier;
import com.iarks.crednote.service.HsnRecyclerAdapter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A fragment representing a list of Items.
 */
public class HsnListFragment extends Fragment implements TaxAmountCarrier, HsnDetailsCarrier {

    List<HsnDetails> hsnDetails;

    HsnRecyclerAdapter adapter;

    public HsnListFragment()
    {
        hsnDetails = new ArrayList<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editable_list, container, false);

        Button addViews = view.findViewById(R.id.addView);
        addViews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hsnDetails.add(new HsnDetails());
                adapter.notifyItemRangeInserted(hsnDetails.size()-1, 1);
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.list);
        // Set the adapter

        if (recyclerView != null) {
            Context context = view.getContext();
            LinearLayoutManager lm = new LinearLayoutManager(context);
            lm.setOrientation(RecyclerView.VERTICAL);
            recyclerView.setLayoutManager(lm);
            adapter = new HsnRecyclerAdapter(hsnDetails);
            recyclerView.setAdapter(adapter);
            adapter.notifyItemRangeInserted(0, hsnDetails.size());
        }
        return view;
    }

    @Override
    public List<HsnDetails> GetHsnDetails() {
        return hsnDetails.stream().filter(hsnDetails -> hsnDetails.isLocked()).collect(Collectors.toList());
    }

    @Override
    public BigDecimal getTotalTaxableAmount() {
        BigDecimal totalTaxableAmount = BigDecimal.ZERO.setScale(2, RoundingMode.DOWN);
        for (HsnDetails hsnDetail : hsnDetails) {
            if (hsnDetail.isLocked())
            {
                totalTaxableAmount = totalTaxableAmount.add(hsnDetail.getTaxableAmount());
            }
        }
        return totalTaxableAmount.setScale(2, RoundingMode.DOWN);
    }

    @Override
    public BigDecimal getTotalCentralTaxAmount() {
        BigDecimal totalTaxableAmount = BigDecimal.ZERO.setScale(2, RoundingMode.DOWN);
        for (HsnDetails hsnDetail : hsnDetails) {
            if (hsnDetail.isLocked())
            {
                totalTaxableAmount = totalTaxableAmount.add(hsnDetail.getCentralTaxAmount());
            }
        }
        return totalTaxableAmount.setScale(2, RoundingMode.DOWN);
    }

    @Override
    public BigDecimal getTotalStateTaxAmount() {
        BigDecimal totalTaxableAmount = BigDecimal.ZERO.setScale(2, RoundingMode.DOWN);
        for (HsnDetails hsnDetail : hsnDetails) {
            if (hsnDetail.isLocked())
            {
                totalTaxableAmount = totalTaxableAmount.add(hsnDetail.getStateTaxAmount());
            }
        }
        return totalTaxableAmount.setScale(2, RoundingMode.DOWN);
    }

    @Override
    public BigDecimal getTotalTaxAmount() {
        BigDecimal totalTaxableAmount = BigDecimal.ZERO.setScale(2, RoundingMode.DOWN);
        for (HsnDetails hsnDetail : hsnDetails) {
            if (hsnDetail.isLocked())
            {
                totalTaxableAmount = totalTaxableAmount.add(hsnDetail.getTotalTaxAmount());
            }
        }
        return totalTaxableAmount.setScale(2, RoundingMode.DOWN);
    }
}