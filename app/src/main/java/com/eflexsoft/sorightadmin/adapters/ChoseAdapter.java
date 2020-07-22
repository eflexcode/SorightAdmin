package com.eflexsoft.sorightadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eflexsoft.sorightadmin.R;
import com.eflexsoft.sorightadmin.UploadActivity;
import com.eflexsoft.sorightadmin.model.ChoseItem;

import java.util.List;

public class ChoseAdapter extends RecyclerView.Adapter<ChoseAdapter.ChoseAdapterViewHolder> {

    List<ChoseItem> choseItemList;
    Context context;

    public ChoseAdapter(List<ChoseItem> choseItemList, Context context) {
        this.context = context;
        this.choseItemList = choseItemList;
    }

    @NonNull
    @Override
    public ChoseAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChoseAdapterViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chose_upload_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChoseAdapterViewHolder holder, int position) {

        ChoseItem choseItem = choseItemList.get(position);
        holder.itemName.setText(choseItem.getName());
        holder.relativeLayout.setBackgroundResource(choseItem.getColor());
        holder.itemView.setOnClickListener(v -> {


            context.startActivity(new Intent(context, UploadActivity.class).putExtra("name",choseItem.getName()));

        });
    }

    @Override
    public int getItemCount() {
        return choseItemList.size();
    }

    public class ChoseAdapterViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout relativeLayout;
        TextView itemName;

        public ChoseAdapterViewHolder(@NonNull View itemView) {
            super(itemView);

            relativeLayout = itemView.findViewById(R.id.chose_background);
            itemName = itemView.findViewById(R.id.chose_text);


        }
    }
}
