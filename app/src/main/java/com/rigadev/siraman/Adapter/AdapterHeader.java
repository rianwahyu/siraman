package com.rigadev.siraman.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


import com.rigadev.siraman.Model.DataHeader;
import com.rigadev.siraman.R;
import com.rigadev.siraman.ui.riwayat.RiwayatFragment;
import com.rigadev.siraman.util.HeaderManager;
import com.rigadev.siraman.util.ItemClickListener;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;


public class AdapterHeader extends RecyclerView.Adapter<AdapterHeader.ViewHolder> {

    public Context context;
    ViewHolder viewHolder;
    private List<DataHeader> evenprolist ;

    private ItemClickListener clickListener;
    RiwayatFragment fragment;
    int row_index=-1;

    public AdapterHeader(Context context, List<DataHeader> model,  RiwayatFragment fragment) {

        this.context = context;
        this.evenprolist = model;
        this.fragment = fragment;
    }

    public void setClickListener(ItemClickListener clickListener){
        this.clickListener=clickListener;
    }

    // Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a layout
                    View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.adapter_history_header, parent, false);

        viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Called by RecyclerView to display the data at the specified position.


    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        DataHeader dataBarang = evenprolist.get(position);
        viewHolder.textInvoice.setText("#"+dataBarang.getInvoice());
        viewHolder.textSales.setText(NgekekiKoma(dataBarang.getSalesValue()));
        viewHolder.textDate.setText(dataBarang.getTime());

        viewHolder.textStore.setText(dataBarang.getStore());

        final String invoice = dataBarang.getInvoice().toString();
        final String store = dataBarang.getStore();
        final String user = dataBarang.getUser();
        final String time = dataBarang.getTime();
        final String sales = dataBarang.getSalesValue();
        final String paid = dataBarang.getPaidValue();
        final String returnValue = dataBarang.getReturnValue();
        final String year = dataBarang.getYear();

        String toko = null;

        if (store.equals("201")){
            toko="Siraman";
        }

        viewHolder.textStore.setText(toko);

        viewHolder.cardHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HeaderManager(context).header(invoice,store,user,time,sales,paid,returnValue,year);
                //if (clickListener != null) clickListener.onClick(v, position);
                callValue(invoice,v);
                row_index=position;
                notifyDataSetChanged();
            }
        });

        if(row_index==position){
            viewHolder.constraintHeader.setBackgroundColor(Color.parseColor("#FFEA00"));
        }
        else {
            viewHolder.constraintHeader.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

    }

    private void callValue(String invoice, View v) {
        ((RiwayatFragment)fragment).callHistoryValue(invoice);
    }

    //Returns the total number of items in the data set hold by the adapter.
    @Override
    public int getItemCount() {
        return evenprolist.size();
    }

    // initializes some private fields to be used by RecyclerView.
    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardHeader;

        TextView textInvoice, textSales, textDate, textStore;
        ConstraintLayout constraintHeader;
        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            textInvoice = (TextView) itemLayoutView.findViewById(R.id.textInvoice);
            textSales = (TextView) itemLayoutView.findViewById(R.id.textTotalSales);
            textDate = (TextView) itemLayoutView.findViewById(R.id.textDate);
            textStore = (TextView) itemLayoutView.findViewById(R.id.textStore);
            cardHeader = (CardView) itemLayoutView.findViewById(R.id.cardHistoryHeader);
            constraintHeader = itemLayoutView.findViewById(R.id.constraintHeader);
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