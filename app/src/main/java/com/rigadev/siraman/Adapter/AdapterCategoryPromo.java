package com.rigadev.siraman.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.rigadev.siraman.Model.DataCategory2;
import com.rigadev.siraman.Model.DataCategory3;
import com.rigadev.siraman.R;
import com.rigadev.siraman.util.ItemCategoryCoffeeListener;
import com.rigadev.siraman.util.ItemCategoryPromoListener;

import java.util.List;


public class AdapterCategoryPromo extends RecyclerView.Adapter<AdapterCategoryPromo.ViewHolder> {

    public Context context;
    ViewHolder viewHolder;
    private List<DataCategory3> evenprolist ;
    int row_index=-1;

    private ItemCategoryPromoListener clickListener;

    public AdapterCategoryPromo(Context context, List<DataCategory3> model) {

        this.context = context;
        this.evenprolist = model;
    }

    public void setCategoryPrommoListener(ItemCategoryPromoListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a layout
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.adapter_category, parent, false);

        viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        DataCategory3 dataBarang = evenprolist.get(position);

        viewHolder.txtNameCategory.setText(dataBarang.getNamaCategory().toUpperCase());
        viewHolder.txtNameCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) clickListener.onClickCategoryPromo(view, position);
                row_index=position;
                notifyDataSetChanged();
            }
        });

        if(row_index==position){
            viewHolder.constraintCategory.setBackgroundColor(Color.parseColor("#FFEA00"));
        }
        else
        {
            viewHolder.constraintCategory.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }

    }

    //Returns the total number of items in the data set hold by the adapter.
    @Override
    public int getItemCount() {
        return evenprolist.size();
    }

    // initializes some private fields to be used by RecyclerView.
    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNameCategory;
        ConstraintLayout constraintCategory;
        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtNameCategory = (TextView) itemLayoutView.findViewById(R.id.textNameCategory);
            constraintCategory = itemLayoutView.findViewById(R.id.constraintCategory);
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}

