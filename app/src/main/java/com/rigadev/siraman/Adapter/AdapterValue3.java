package com.rigadev.siraman.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.rigadev.siraman.Model.DataValue2;
import com.rigadev.siraman.R;
import com.rigadev.siraman.util.SQLiteHelpers;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


public class AdapterValue3 extends RecyclerView.Adapter<AdapterValue3.ViewHolder> {

    public Context context;
    ViewHolder viewHolder;
    private List<DataValue2> evenprolist ;
    Typeface mtypeFace, mtypeFaceBold;

    SQLiteHelpers sqLiteHelpers = new SQLiteHelpers(context);

    public AdapterValue3(Context context, List<DataValue2> model) {

        this.context = context;
        this.evenprolist = model;
    }

    // Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a layout
                    View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.adapter_history_value2, parent, false);

        viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Called by RecyclerView to display the data at the specified position.
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        DataValue2 data = evenprolist.get(position);
        viewHolder.textName.setText(data.getName());
        viewHolder.textQty.setText(data.getQty());
        viewHolder.textPrice.setText(NgekekiKoma(data.getPrice()));
        viewHolder.textTotalItem.setText(NgekekiKoma(data.getTotal()));
        viewHolder.textInvoice.setText(data.getBarcode());

    }


    //Returns the total number of items in the data set hold by the adapter.
    @Override
    public int getItemCount() {
        return evenprolist.size();
    }

    // initializes some private fields to be used by RecyclerView.
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView textName, textQty, textPrice, textTotalItem, textInvoice;
        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            textName = (TextView) itemLayoutView.findViewById(R.id.textNameItem);
            textQty = (TextView) itemLayoutView.findViewById(R.id.textQty);
            textPrice = (TextView) itemLayoutView.findViewById(R.id.textPriceItem);
            textTotalItem = (TextView) itemLayoutView.findViewById(R.id.textTotalItem);
            textInvoice = (TextView) itemLayoutView.findViewById(R.id.textInvoice);
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