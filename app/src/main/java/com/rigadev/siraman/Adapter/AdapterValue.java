package com.rigadev.siraman.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.rigadev.siraman.Model.DataValue;
import com.rigadev.siraman.R;
import com.rigadev.siraman.util.SQLiteHelpers;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


public class AdapterValue extends RecyclerView.Adapter<AdapterValue.ViewHolder> {

    public Context context;
    ViewHolder viewHolder;
    private List<DataValue> evenprolist ;
    Typeface mtypeFace, mtypeFaceBold;

    SQLiteHelpers sqLiteHelpers = new SQLiteHelpers(context);

    public AdapterValue(Context context, List<DataValue> model) {

        this.context = context;
        this.evenprolist = model;
    }

    // Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a layout
                    View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.adapter_history_value, parent, false);

        viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        /*mtypeFace = Typeface.createFromAsset(context.getAssets(),
                "font/Montserrat-Light.ttf");
        mtypeFaceBold = Typeface.createFromAsset(context.getAssets(),
                "font/Montserrat-SemiBold.ttf");*/

        DataValue data = evenprolist.get(position);
        viewHolder.textName.setText(data.getName());
        viewHolder.textQty.setText(data.getQty());
        viewHolder.textPrice.setText(NgekekiKoma(data.getPrice()));
        viewHolder.textTotalItem.setText(NgekekiKoma(data.getTotal()));

    }


    //Returns the total number of items in the data set hold by the adapter.
    @Override
    public int getItemCount() {
        return evenprolist.size();
    }

    // initializes some private fields to be used by RecyclerView.
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtName, txtStock, txtPrice;
        CardView cardHeader;

        TextView textName, textQty, textPrice, textTotalItem;
        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            textName = (TextView) itemLayoutView.findViewById(R.id.textNameItem);
            textQty = (TextView) itemLayoutView.findViewById(R.id.textQty);
            textPrice = (TextView) itemLayoutView.findViewById(R.id.textPriceItem);
            textTotalItem = (TextView) itemLayoutView.findViewById(R.id.textTotalItem);
        }
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