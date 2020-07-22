package com.eflexsoft.sorightadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class UploadActivity extends DaggerAppCompatActivity {
    Toolbar toolbar;

    Intent intent;
    ImageView imageView;
    EditText post;
    Button post_btn;
    Uri uploadUri;
    StorageReference reference;
    AVLoadingIndicatorView avLoadingIndicatorView;
    String name;

    @Inject
    FirebaseDatabase firebaseDatabase;

    @Inject
    FirebaseStorage firebaseStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        setContentView(R.layout.activity_uplaod);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        intent = getIntent();
        setTitle(intent.getStringExtra("name"));
        name = intent.getStringExtra("name");
        imageView = findViewById(R.id.upload_image);
        post = findViewById(R.id.upload_title);
        post_btn = findViewById(R.id.post_btn);
        avLoadingIndicatorView = findViewById(R.id.progress);

        post.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (text.isEmpty()){
                    post_btn.setBackgroundResource(R.drawable.post_btn_background);
                }else {
                    post_btn.setBackgroundResource(R.drawable.login_btn_background);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        reference = FirebaseStorage.getInstance().getReference("PostImages");

    }

    public void getPhoto(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK){
            assert data != null;
            imageView.setImageURI(data.getData());

            uploadUri = data.getData();
        }

    }


    private String getMineType(Uri uri){

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public void doPost(View view) {

        String getPost = post.getText().toString();

        if (getPost.isEmpty() || uploadUri == null){
            Toast.makeText(this, "post is empty or you haven't picked an image", Toast.LENGTH_LONG).show();
            return;
        }

        avLoadingIndicatorView.setVisibility(View.VISIBLE);
        StorageReference storageReference = reference.child(getMineType(uploadUri)+"."+System.currentTimeMillis());
        UploadTask uploadTask = storageReference.putFile(uploadUri);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                if(!task.isSuccessful()){
                    throw task.getException();
                }

                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){

                    String getDownloadUrl = task.getResult().toString();

                    DatabaseReference databaseReference = firebaseDatabase.getReference("Post")
                            .child(name);

                    Map<String,String> objectMap = new HashMap<>();
                    objectMap.put("text",getPost);
                    objectMap.put("imageUri",getDownloadUrl);
                    objectMap.put("catergoryName",name);

                    databaseReference.push().setValue(objectMap).addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()){
                            Toast.makeText(UploadActivity.this, "post uploaded", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(e -> {
                        avLoadingIndicatorView.setVisibility(View.GONE);
                        Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                avLoadingIndicatorView.setVisibility(View.GONE);
                Toast.makeText(UploadActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
}
