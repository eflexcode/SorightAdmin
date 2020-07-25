package com.eflexsoft.sorightadmin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eflexsoft.sorightadmin.R;
import com.eflexsoft.sorightadmin.model.Message;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    List<Message> messageList = new ArrayList<>();

    Context context;
    String imageUrl;

    public MessageAdapter(Context context, String imageUrl) {
        this.context = context;
        this.imageUrl = imageUrl;
    }

    private static final int me = 1;
    private static final int you = 2;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == me) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_me_layout, parent, false);
            return new ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_you_layout, parent, false);
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Message message = messageList.get(position);

        if (position == messageList.size() - 1) {
            holder.isSeen.setVisibility(View.VISIBLE);
            if (message.getIsSeen().equals("yes")) {
                holder.isSeen.setText("seen");
            } else {
                holder.isSeen.setText("sent");
            }
        } else {
            holder.isSeen.setVisibility(View.GONE);
        }

        holder.message.setText(message.getMessage());

        if (imageUrl.equals("default")) {
            holder.propic.setImageResource(R.drawable.no_p);
        } else {
            Glide.with(context).load(imageUrl).into(holder.propic);
        }

        if (message.getImageUrl().equals("default")){
            holder.message.setVisibility(View.VISIBLE);
            holder.messageImage.setVisibility(View.GONE);
        }else {
            Glide.with(context).load(message.getImageUrl()).into(holder.messageImage);
            holder.message.setVisibility(View.GONE);
            holder.messageImage.setVisibility(View.VISIBLE);
        }

    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
      notifyDataSetChanged();
    }
    public void setMessageListupdate(List<Message> messageList) {
        this.messageList = messageList;
       notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        CircleImageView propic;
        TextView message;
        ImageView messageImage;
        TextView isSeen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            propic = itemView.findViewById(R.id.pro_pic);
            message = itemView.findViewById(R.id.messageTextChat);
            messageImage = itemView.findViewById(R.id.imageChat);
            isSeen = itemView.findViewById(R.id.isseen);

        }
    }

    @Override
    public int getItemViewType(int position) {

        Message message = messageList.get(position);
        if (message.getSenderId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            return me;
        } else {
            return you;
        }

    }
}
