package com.rigadev.siraman.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.rigadev.siraman.Model.DataItem2;
import com.rigadev.siraman.Model.DataItem3;
import com.rigadev.siraman.R;
import com.rigadev.siraman.ui.home.HomeFragment;
import com.rigadev.siraman.util.ItemClickCoffeeListener;
import com.rigadev.siraman.util.ItemClickPromoListener;
import com.rigadev.siraman.util.MyConfig;

import java.util.List;


public class AdapterItem3 extends RecyclerView.Adapter<AdapterItem3.ViewHolder> {

    public Context context;
    ViewHolder viewHolder;
    private List<DataItem3> evenprolist ;

    private ItemClickPromoListener clickListener;

    HomeFragment fragment;

    public AdapterItem3(Context context, List<DataItem3> model, HomeFragment fragment) {

        this.context = context;
        this.evenprolist = model;
        this.fragment = fragment;
    }

    public void setClickPromoListener(ItemClickPromoListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    // Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a layout
                    View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.adapter_item, parent, false);

        viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        DataItem3 dataBarang = evenprolist.get(position);
        viewHolder.txtName.setText(dataBarang.getName());
        /*viewHolder.txtStock.setText(dataBarang.getQty());*/
        viewHolder.txtPrice.setText(MyConfig.formatNumberComma(dataBarang.getGp1()));

       /* final String name =  dataBarang.getName();
        final String stock = dataBarang.getQty();
        final String price = dataBarang.getGp1();
        final String barcode = dataBarang.getBarcode();
        final String subCategory = dataBarang.getSubCategory();
        final String aliasName = dataBarang.getAlias_name();*/

        viewHolder.cardItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickListener != null) clickListener.onClickPromo(v, position);
                //viewItem(barcode,name,stock,price, v);
                //((HomeFragment)fragment).viewItem(barcode, name, stock, price, v, subCategory, aliasName);

            }
        });

        Glide.with(context)
                .load(evenprolist.get(position).getSource())
                .apply(new RequestOptions().placeholder(R.drawable.food_placeholder).error(R.drawable.food_placeholder))
                .into(viewHolder.imageItem);
    }

    //Returns the total number of items in the data set hold by the adapter.
    @Override
    public int getItemCount() {
        return evenprolist.size();
    }

    // initializes some private fields to be used by RecyclerView.
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtStock, txtPrice;
        CardView cardItem;
        ImageView imageItem;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            txtName = itemLayoutView.findViewById(R.id.textNameItem);
            /*txtStock = itemLayoutView.findViewById(R.id.textStockItem);*/
            txtPrice = itemLayoutView.findViewById(R.id.textPriceItem);
            cardItem = itemLayoutView.findViewById(R.id.cardItem);
            imageItem = itemLayoutView.findViewById(R.id.imageItem);
        }
    }

}

