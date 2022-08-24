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
import com.iarks.crednote.abstractions.GoodsListCarrier;
import com.iarks.crednote.models.Good;
import com.iarks.crednote.models.GoodsAmountCarrier;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A fragment representing a list of Items.
 */
public class GoodsListFragment extends Fragment implements GoodsAmountCarrier, GoodsListCarrier {

    List<Good> TempGoods;

    GoodsRecyclerViewAdapter adapter;

    public GoodsListFragment(){
        TempGoods = new ArrayList<>();
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
                TempGoods.add(new Good());
                adapter.notifyItemRangeInserted(TempGoods.size()-1, 1);
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.list);
        // Set the adapter

        if (recyclerView != null) {
            Context context = view.getContext();
            LinearLayoutManager lm = new LinearLayoutManager(context);
            lm.setOrientation(RecyclerView.VERTICAL);
            recyclerView.setLayoutManager(lm);
            adapter = new GoodsRecyclerViewAdapter(TempGoods);
            recyclerView.setAdapter(adapter);
            adapter.notifyItemRangeInserted(0, TempGoods.size());
        }
        return view;
    }

    @Override
    public List<Good> getGoodDetails() {
        return TempGoods.stream().filter(good -> good.isLocked() == true).collect(Collectors.toList());
    }

    @Override
    public BigDecimal getGoodsTotal() {
        BigDecimal totalTaxableAmount = BigDecimal.ZERO.setScale(2, RoundingMode.DOWN);
        for (Good good : TempGoods) {
            if (good.isLocked())
            {
                totalTaxableAmount = totalTaxableAmount.add(good.getAmount());
            }
        }
        return totalTaxableAmount.setScale(2, RoundingMode.DOWN);
    }
}