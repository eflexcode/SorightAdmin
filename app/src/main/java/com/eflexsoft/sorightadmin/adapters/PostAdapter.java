package com.eflexsoft.sorightadmin.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.eflexsoft.sorightadmin.FullImageActivity;
import com.eflexsoft.sorightadmin.R;
import com.eflexsoft.sorightadmin.model.Post;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    List<Post> posts;
    Context context;

    public PostAdapter(List<Post> posts, Context context) {
        this.posts = posts;
        ;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Post post = posts.get(position);

        holder.textView.setText(post.getText());
        Glide.with(context).load(post.getImageUri()).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, FullImageActivity.class)
                        .putExtra("text", post.getText())
                        .putExtra("imageUri", post.getImageUri())
                        .putExtra("catergoryName", post.getCatergoryName()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image_post);
            textView = itemView.findViewById(R.id.post_summery);

        }
    }
}
