package com.iarks.crednote.presentation.fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.iarks.crednote.R;
import com.iarks.crednote.models.Good;
import com.iarks.crednote.service.GoodsFormUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

// main class
public class GoodsRecyclerViewAdapter extends RecyclerView.Adapter<GoodsRecyclerViewAdapter.ViewHolder> {

    private final List<Good> goods;

    // inject a reference to the goods list here. This is a by reference list that is updated in the parent as updates happen here
    public GoodsRecyclerViewAdapter(List<Good> goods) {
        this.goods = goods;
    }

    // inflates the view for 1 row in the recycler view
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_goods, parent, false);
        return new ViewHolder(rowView,
                new GoodsFormUtil.GoodsDescriptionChangeListener(),
                new GoodsFormUtil.GoodsCodeChangeListener(),
                new GoodsFormUtil.GoodsQuantityChangeListener(),
                new GoodsFormUtil.GoodsRateChangeListener(),
                new GoodsFormUtil.GoodsUnitChangeListener(),
                new GoodsFormUtil.GoodsDiscountChangeListener(),
                new GoodsFormUtil.GoodsAmountChangeListener());
    }

    // called to attach data to the view that was inflated earlier
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.updateListenerPosition(goods.get(position));
        holder.updateView(position);
    }



    @Override
    public int getItemCount() {
        return goods.size();
    }

    /// This is what describes an item in the list view
    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextInputLayout descriptionOfGoods;
        private final TextInputLayout hsn;
        private final TextInputLayout quantity;
        private final TextInputLayout rate;
        private final TextInputLayout per;
        private final TextInputLayout disc;
        private final TextInputLayout amount;
        private final Button evaluate;
        private final Button save;

        private final GoodsFormUtil.GoodsDescriptionChangeListener goodsDescriptionChangeListener;
        private final GoodsFormUtil.GoodsCodeChangeListener goodsCodeChangeListener;
        private final GoodsFormUtil.GoodsQuantityChangeListener goodsQuantityChangeListener;
        private final GoodsFormUtil.GoodsRateChangeListener goodsRateChangeListener;
        private final GoodsFormUtil.GoodsUnitChangeListener goodsUnitChangeListener;
        private final GoodsFormUtil.GoodsDiscountChangeListener goodsDiscountChangeListener;
        private final GoodsFormUtil.GoodsAmountChangeListener goodsAmountChangeListener;

        public ViewHolder(View view,
                          GoodsFormUtil.GoodsDescriptionChangeListener goodsDescriptionChangeListener,
                          GoodsFormUtil.GoodsCodeChangeListener goodsCodeChangeListener,
                          GoodsFormUtil.GoodsQuantityChangeListener goodsQuantityChangeListener,
                          GoodsFormUtil.GoodsRateChangeListener goodsRateChangeListener,
                          GoodsFormUtil.GoodsUnitChangeListener goodsUnitChangeListener,
                          GoodsFormUtil.GoodsDiscountChangeListener goodsDiscountChangeListener,
                          GoodsFormUtil.GoodsAmountChangeListener goodsAmountChangeListener)
        {
            super(view);
            this.goodsDescriptionChangeListener = goodsDescriptionChangeListener;
            this.goodsCodeChangeListener = goodsCodeChangeListener;
            this.goodsQuantityChangeListener = goodsQuantityChangeListener;
            this.goodsRateChangeListener = goodsRateChangeListener;
            this.goodsUnitChangeListener = goodsUnitChangeListener;
            this.goodsDiscountChangeListener = goodsDiscountChangeListener;
            this.goodsAmountChangeListener = goodsAmountChangeListener;

            // get hold of individual view
            descriptionOfGoods = view.findViewById(R.id.descriptionOfGoods);
            descriptionOfGoods.getEditText().addTextChangedListener(this.goodsDescriptionChangeListener);
            hsn = view.findViewById(R.id.hsn);
            hsn.getEditText().addTextChangedListener(this.goodsCodeChangeListener);
            quantity = view.findViewById(R.id.quantity);
            quantity.getEditText().addTextChangedListener(this.goodsQuantityChangeListener);
            rate = view.findViewById(R.id.rate);
            rate.getEditText().addTextChangedListener(this.goodsRateChangeListener);
            per = view.findViewById(R.id.per);
            per.getEditText().addTextChangedListener(this.goodsUnitChangeListener);
            disc = view.findViewById(R.id.disc);
            disc.getEditText().addTextChangedListener(this.goodsDiscountChangeListener);
            amount = view.findViewById(R.id.amount);
            amount.getEditText().addTextChangedListener(this.goodsAmountChangeListener);

            Button removeView = view.findViewById(R.id.removeGood);
            removeView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    goods.remove(getBindingAdapterPosition());
                    notifyItemRemoved(getBindingAdapterPosition());
                }
            });

            evaluate = view.findViewById(R.id.recalculate);
            evaluate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getBindingAdapterPosition();
                    updateView(position);
                    notifyItemChanged(getBindingAdapterPosition());
                }
            });

            save = view.findViewById(R.id.save);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getBindingAdapterPosition();
                    Good savedGood = goods.get(position);
                    toggleView(savedGood, view);
                    notifyItemChanged(getBindingAdapterPosition());
                }
            });
        }

        public void toggleView(Good good, View button)
        {
            if(!good.isLocked())
            {
                good.setDescription(Objects.requireNonNull(descriptionOfGoods.getEditText().getText()).toString());
                good.setHsn(Integer.parseInt(hsn.getEditText().getText().toString()));
                good.setQuantity(new BigDecimal(quantity.getEditText().getText().toString()).setScale(2, RoundingMode.DOWN));
                good.setRate(new BigDecimal(rate.getEditText().getText().toString()).setScale(2, RoundingMode.DOWN));
                good.setUnit(per.getEditText().getText().toString());
                good.setDiscountPercentage(new BigDecimal(disc.getEditText().getText().toString()).setScale(2, RoundingMode.DOWN));
                good.setAmount(new BigDecimal(amount.getEditText().getText().toString()).setScale(2, RoundingMode.DOWN));
                good.lock();
                ((Button)button).setText(R.string.label_edit);
            }
            else
            {
                good.unlock();
                ((Button)button).setText(R.string.label_save);
            }
        }

        public void updateView(int position) {
            descriptionOfGoods.getEditText().setText(goods.get(position).getDescriptionOfGoods());
            hsn.getEditText().setText(goods.get(position).getHsnCode());
            quantity.getEditText().setText(String.valueOf(goods.get(position).getQuantity()));
            rate.getEditText().setText(String.valueOf(goods.get(position).getRate()));
            per.getEditText().setText(goods.get(position).getUnit());
            disc.getEditText().setText(String.valueOf(goods.get(position).getDiscount()));
            amount.getEditText().setText(String.valueOf(goods.get(position).getAmount()));

            try {
                if (goods.get(getBindingAdapterPosition()).isLocked()) {
                    save.setText(R.string.label_edit);
                    evaluate.setEnabled(false);
                } else {
                    save.setText(R.string.label_save);
                    evaluate.setEnabled(true);
                }
            }catch (Exception ignore){}

            if(goods.get(position).isLocked())
            {
                enableFields(false);
            }
            else
            {
                enableFields(true);
            }
        }

        private void enableFields(boolean isEnabled) {
            descriptionOfGoods.getEditText().setEnabled(isEnabled);
            hsn.getEditText().setEnabled(isEnabled);
            quantity.getEditText().setEnabled(isEnabled);
            rate.getEditText().setEnabled(isEnabled);
            per.getEditText().setEnabled(isEnabled);
            disc.getEditText().setEnabled(isEnabled);
            amount.getEditText().setEnabled(isEnabled);
        }

        // public method that updates the listener positions. This tells the listener which actual object is manipulated via the view
        public void updateListenerPosition(Good good) {
            goodsDescriptionChangeListener.updatePosition(good);
            goodsCodeChangeListener.updatePosition(good);
            goodsQuantityChangeListener.updatePosition(good);
            goodsRateChangeListener.updatePosition(good);
            goodsUnitChangeListener.updatePosition(good);
            goodsDiscountChangeListener.updatePosition(good);
            goodsAmountChangeListener.updatePosition(good);
        }
    }
}