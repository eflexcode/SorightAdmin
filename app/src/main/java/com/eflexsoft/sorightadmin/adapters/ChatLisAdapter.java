package com.eflexsoft.sorightadmin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eflexsoft.sorightadmin.R;
import com.eflexsoft.sorightadmin.model.User;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatLisAdapter extends RecyclerView.Adapter<ChatLisAdapter.ViewHolder> {

    List<User> userList = new ArrayList<>();
    Context context;

    public ChatLisAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_list,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        User user = userList.get(position);
        holder.name.setText(user.getName());
        if (user.getImageUrl().equals("default")){
            holder.proPic.setImageResource(R.drawable.no_p);
        }else {
            Glide.with(context).load(user.getImageUrl()).into(holder.proPic);
        }
        holder.last.setText(user.getLastMessage());

    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView proPic;
        TextView name;
        TextView last;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            proPic = itemView.findViewById(R.id.chat_image);
            name = itemView.findViewById(R.id.chat_name);
            last = itemView.findViewById(R.id.chat_last);

        }
    }
}
