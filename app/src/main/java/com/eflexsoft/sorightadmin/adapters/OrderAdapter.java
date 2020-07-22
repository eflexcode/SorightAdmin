package com.eflexsoft.sorightadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eflexsoft.sorightadmin.OrderDetailsActivity;
import com.eflexsoft.sorightadmin.R;
import com.eflexsoft.sorightadmin.model.OrderList;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    List<OrderList> orderLists = new ArrayList<>();

    public void setOrderLists(List<OrderList> orderLists) {
        this.orderLists = orderLists;
       notifyDataSetChanged();
    }

    Context context;

    public OrderAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        OrderList orderList = orderLists.get(position);

        holder.name.setText(orderList.getName());
        holder.date.setText("from "+orderList.getStartDate()+" to "+orderList.getEndDate());
        holder.price.setText(orderList.getPackage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,OrderDetailsActivity.class);
                intent.putExtra("name", "Name : "+orderList.getName());
                intent.putExtra("Sdate","Start Date : "+orderList.getStartDate());
                intent.putExtra("price","Package : "+orderList.getPackage());
                intent.putExtra("id","Id : "+orderList.getId());
                intent.putExtra("email","Email : "+orderList.getEmail());
                intent.putExtra("idforDelete",orderList.getId());
                intent.putExtra("date",orderList.getStartDate());
                intent.putExtra("Edate","End Date : "+orderList.getEndDate());
                intent.putExtra("Phone","Phone Number : "+orderList.getPhoneNumber());

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return orderLists.size();
    }

   static class ViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView price;
        TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            price = itemView.findViewById(R.id.price);
            date = itemView.findViewById(R.id.date);

        }
    }

}
