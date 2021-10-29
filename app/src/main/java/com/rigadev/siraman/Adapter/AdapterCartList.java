package com.rigadev.siraman.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.rigadev.siraman.Model.DataCart;
import com.rigadev.siraman.R;
import com.rigadev.siraman.util.MyConfig;

import java.util.List;

public class AdapterCartList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<DataCart> loc ;


    public AdapterCartList(Context context, List<DataCart> loc) {
        this.context = context;
        this.loc = loc;
    }


    int row_index=-1;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.adapter_struk_order_list, viewGroup, false);

        ViewHolderRow viewHolder = new ViewHolderRow(itemLayoutView);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof ViewHolderRow) {
            final ViewHolderRow viewHolders = (ViewHolderRow) viewHolder;

            int harga = Integer.parseInt(loc.get(position).getPrice());
            int qty = Integer.parseInt(loc.get(position).getQty());
            int total = qty * harga;

            viewHolders.textNamaKategori.setText(loc.get(position).getAlias_name());
            viewHolders.textHarga.setText("Rp. "+ MyConfig.formatNumberComma(loc.get(position).getPrice())
                    +" x "+loc.get(position).getQty());

            viewHolders.textTotal.setText("Rp. "+MyConfig.formatNumberComma(String.valueOf(total)));

        }
    }


    @Override
    public int getItemCount() {
        return loc == null ? 0 : loc.size();
    }

    public class ViewHolderRow extends RecyclerView.ViewHolder {

        TextView textNamaKategori, textHarga, textJumlah, textTotal ;

        public ViewHolderRow(@NonNull View itemView) {
            super(itemView);

            textNamaKategori = itemView.findViewById(R.id.textNamaKategori);
            textHarga = itemView.findViewById(R.id.textHarga);
            //textJumlah = itemView.findViewById(R.id.textJumlah);
            textTotal = itemView.findViewById(R.id.textTotal);
        }
    }
}
