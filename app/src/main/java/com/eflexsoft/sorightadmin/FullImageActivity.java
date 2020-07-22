package com.eflexsoft.sorightadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eflexsoft.sorightadmin.model.Post;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class FullImageActivity extends DaggerAppCompatActivity {

    PhotoView fullImage;
    TextView fullName;
    Intent intent;
    String categoryReference;

    @Inject
    FirebaseDatabase firebaseDatabase;

    String text;
    String catergoryName;
    AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_image);

        fullImage = findViewById(R.id.image_full);
        fullName = findViewById(R.id.full_text);

        intent = getIntent();


        text = intent.getStringExtra("text");
        String image = intent.getStringExtra("imageUri");
        catergoryName = intent.getStringExtra("catergoryName");

        fullName.setText(text);
        Glide.with(this).load(image).into(fullImage);
    }

    public void delete(View view) {

        DatabaseReference databaseReference = firebaseDatabase.getReference("Post").child(catergoryName);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("Confirm delete")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    Post post = dataSnapshot.getValue(Post.class);
                                    if (post.getText().equals(text)) {
                                        dataSnapshot.getRef().setValue(null);
                                        dialog.dismiss();
                                        finish();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog = builder.create();
        alertDialog.show();


    }
}
