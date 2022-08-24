package com.iarks.crednote.service;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.iarks.crednote.R;
import com.iarks.crednote.models.Good;
import com.iarks.crednote.models.HsnDetails;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

// main class
public class HsnRecyclerAdapter extends RecyclerView.Adapter<HsnRecyclerAdapter.ViewHolder> {

    private final List<HsnDetails> hsnDetails;

    public HsnRecyclerAdapter(List<HsnDetails> goods) {
        this.hsnDetails = goods;
    }

    // inflates the view for 1 row in the recycler view
    @Override
    public HsnRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_hsn, parent, false);
        return new HsnRecyclerAdapter.ViewHolder(rowView,
                new HsnFormUtil.TaxableAmountChangeListener(),
                new HsnFormUtil.CentralTaxRateChangeListener(),
                new HsnFormUtil.StateTaxRateChangeListener());
    }

    @Override
    public void onBindViewHolder(final HsnRecyclerAdapter.ViewHolder holder, int position) {
        holder.updateListenerPosition(hsnDetails.get(position));
        holder.updateView(position);
    }

    @Override
    public int getItemCount() {
        return hsnDetails.size();
    }

    /// This is what describes an item in the list view
    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextInputLayout hsn;
        private TextInputLayout taxableAmount;
        private TextInputLayout centralTaxRate;
        private TextInputLayout centralTaxAmount;
        private TextInputLayout stateTaxRate;
        private TextInputLayout stateTaxAmount;
        private TextInputLayout totalTaxAmount;
        private Button removeView;
        private Button evaluate;
        private Button save;
        private HsnFormUtil.CentralTaxRateChangeListener centralTaxRateChangeListener;
        private HsnFormUtil.TaxableAmountChangeListener taxableAmountChangeListener;
        private HsnFormUtil.StateTaxRateChangeListener stateTaxRateChangeListener;

        public ViewHolder(View view, HsnFormUtil.TaxableAmountChangeListener taxableAmountChangeListener, HsnFormUtil.CentralTaxRateChangeListener centralTaxRateChangeListener, HsnFormUtil.StateTaxRateChangeListener stateTaxRateChangeListener) {
            super(view);

            this.centralTaxRateChangeListener = centralTaxRateChangeListener;
            this.stateTaxRateChangeListener = stateTaxRateChangeListener;
            this.taxableAmountChangeListener = taxableAmountChangeListener;

            hsn = view.findViewById(R.id.hsn);
            taxableAmount = view.findViewById(R.id.taxableAmount);
            taxableAmount.getEditText().addTextChangedListener(this.taxableAmountChangeListener);
            centralTaxRate = view.findViewById(R.id.centralTaxRate);
            centralTaxRate.getEditText().addTextChangedListener(this.centralTaxRateChangeListener);
            centralTaxAmount = view.findViewById(R.id.centralTaxAmount);
            stateTaxRate = view.findViewById(R.id.stateTaxRate);
            stateTaxRate.getEditText().addTextChangedListener(this.stateTaxRateChangeListener);
            stateTaxAmount = view.findViewById(R.id.stateTaxAmount);
            totalTaxAmount = view.findViewById(R.id.totalTaxAmount);
            removeView = view.findViewById(R.id.removeHsn);

            removeView = view.findViewById(R.id.removeHsn);
            removeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hsnDetails.remove(getBindingAdapterPosition());
                    notifyItemRemoved(getBindingAdapterPosition());
                }
            });

            evaluate = view.findViewById(R.id.evaluate);
            evaluate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getBindingAdapterPosition();
                    updateView(position);
                    notifyItemChanged(position);
                }
            });

            save = view.findViewById(R.id.save);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getBindingAdapterPosition();
                    HsnDetails savedHsn = hsnDetails.get(position);
                    toggleView(savedHsn, view);
                    notifyItemChanged(getBindingAdapterPosition());
                }
            });
        }

        private void toggleView(HsnDetails hsnDetail, View view) {
            if(!hsnDetail.isLocked())
            {
                hsnDetail.setHsnCode(Objects.requireNonNull(hsn.getEditText().getText()).toString());
                hsnDetail.setTaxableAmount(new BigDecimal(taxableAmount.getEditText().getText().toString()).setScale(2, RoundingMode.DOWN));
                hsnDetail.setCentralTaxRate(new BigDecimal(centralTaxRate.getEditText().getText().toString()).setScale(2, RoundingMode.DOWN));
                hsnDetail.setCentralTaxAmount(new BigDecimal(centralTaxAmount.getEditText().getText().toString()).setScale(2, RoundingMode.DOWN));
                hsnDetail.setStateTaxRate(new BigDecimal(stateTaxRate.getEditText().getText().toString()).setScale(2, RoundingMode.DOWN));
                hsnDetail.setStateTaxAmount(new BigDecimal(stateTaxAmount.getEditText().getText().toString()).setScale(2, RoundingMode.DOWN));
                hsnDetail.setTotalTaxAmount(new BigDecimal(totalTaxAmount.getEditText().getText().toString()).setScale(2, RoundingMode.DOWN));
                hsnDetail.lock();
                ((Button)view).setText(R.string.label_edit);
            }
            else
            {
                hsnDetail.unlock();
                ((Button)view).setText(R.string.label_save);
            }
        }

        public void updateView(int position) {
            hsn.getEditText().setText(hsnDetails.get(position).getHsnCode());
            taxableAmount.getEditText().setText(String.valueOf(hsnDetails.get(position).getTaxableAmount()));
            centralTaxRate.getEditText().setText(String.valueOf(hsnDetails.get(position).getCentralTaxRate()));
            centralTaxAmount.getEditText().setText(String.valueOf(hsnDetails.get(position).getCentralTaxAmount()));
            stateTaxRate.getEditText().setText(String.valueOf(hsnDetails.get(position).getStateTaxRate()));
            stateTaxAmount.getEditText().setText(String.valueOf(hsnDetails.get(position).getStateTaxAmount()));
            totalTaxAmount.getEditText().setText(String.valueOf(hsnDetails.get(position).getTotalTaxAmount()));

            try {
                if (hsnDetails.get(position).isLocked()) {
                    save.setText(R.string.label_edit);
                } else {
                    save.setText(R.string.label_save);
                }
            }catch (Exception ignore){}

            if(hsnDetails.get(position).isLocked())
            {
                enableFields(false);
            }
            else
            {
                enableFields(true);
            }



        }

        private void enableFields(boolean enable) {
            hsn.setEnabled(enable);
            taxableAmount.setEnabled(enable);
            centralTaxRate.setEnabled(enable);
            centralTaxAmount.setEnabled(enable);
            stateTaxRate.setEnabled(enable);
            stateTaxAmount.setEnabled(enable);
            totalTaxAmount.setEnabled(enable);
            evaluate.setEnabled(enable);
        }

        public void updateListenerPosition(HsnDetails hsnDetail) {
            centralTaxRateChangeListener.updatePosition(hsnDetail);
            stateTaxRateChangeListener.updatePosition(hsnDetail);
            taxableAmountChangeListener.updatePosition(hsnDetail);
        }
    }
}