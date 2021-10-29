package com.rigadev.siraman.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.rigadev.siraman.Model.DataCart;
import com.rigadev.siraman.R;
import com.rigadev.siraman.util.ItemDeleteCartListener;
import com.rigadev.siraman.util.ItemQtyCartListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


public class AdapterCart extends RecyclerView.Adapter<AdapterCart.ViewHolder> {

    public Context context;
    ViewHolder viewHolder;
    private List<DataCart> evenprolist ;
    private ItemDeleteCartListener clickListener;
    private ItemQtyCartListener itemClickListener;

    public AdapterCart(Context context, List<DataCart> model) {
        this.context = context;
        this.evenprolist = model;
    }

    public void setClickListener(ItemDeleteCartListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public void setQtyCatClickListener(ItemQtyCartListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    // Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a layout
                    View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.adapter_cart, parent, false);

        viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        DataCart dataBarang = evenprolist.get(position);
        viewHolder.txtName.setText(dataBarang.getName());
        viewHolder.txtQty.setText(dataBarang.getQty());
        viewHolder.txtPrice.setText(NgekekiKoma(dataBarang.getPrice()));

        viewHolder.consDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) clickListener.onClick2(view, position);
            }
        });

        viewHolder.linearCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (itemClickListener != null) itemClickListener.onClick4(view, position);
            }
        });
    }

    //Returns the total number of items in the data set hold by the adapter.
    @Override
    public int getItemCount() {
        return evenprolist.size();
    }

    // initializes some private fields to be used by RecyclerView.
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtQty, txtPrice;
        ConstraintLayout consDelete;
        LinearLayout linearCart;
        public RelativeLayout viewBackground, viewForeground;
        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            txtName = (TextView) itemLayoutView.findViewById(R.id.textNameItem);
            txtQty = (TextView) itemLayoutView.findViewById(R.id.textQty);
            txtPrice = (TextView) itemLayoutView.findViewById(R.id.textPriceItem);
            consDelete = itemLayoutView.findViewById(R.id.consDelete);
            linearCart = itemLayoutView.findViewById(R.id.linearCart);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }


    public String NgekekiKoma(String s){
        String originalString = s.toString();

        Long longval;
        if (originalString.contains(",")) {
            originalString = originalString.replaceAll(",", "");
        }
        longval = Long.parseLong(originalString);

        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        formatter.applyPattern("#,###,###,###");
        String formattedString = formatter.format(longval);

        return formattedString;
    }
}

