package com.eflexsoft.sorightadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PathPermission;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eflexsoft.sorightadmin.adapters.MessageAdapter;
import com.eflexsoft.sorightadmin.model.Message;
import com.eflexsoft.sorightadmin.model.Post;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends DaggerAppCompatActivity {

    EditText messageText;
    ImageButton send;
    CircleImageView propic;
    TextView name;

    SwipeRefreshLayout swipeRefreshLayout;

    @Inject
    FirebaseAuth firebaseAuth;

    @Inject
    FirebaseDatabase firebaseDatabase;

    StorageReference reference;

    ValueEventListener valueEventListener;
    DatabaseReference setiseen;

    String id;
    Intent intent;
    String username;
    String imageUrl;

    Uri uploadUri;

    AlertDialog alertDialog;

    int size = 50;

    List<Message> messageList = new ArrayList<>();

    RecyclerView recyclerView;

    MessageAdapter messageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));
        setContentView(R.layout.activity_message);

        recyclerView = findViewById(R.id.messageRecycler);

        messageText = findViewById(R.id.messageText);
        send = findViewById(R.id.send);
        name = findViewById(R.id.username);
        propic = findViewById(R.id.Pro_pic);
        swipeRefreshLayout = findViewById(R.id.swipe);

        intent = getIntent();
        id = intent.getStringExtra("id");
        username = intent.getStringExtra("name");
        imageUrl = intent.getStringExtra("image");

        name.setText(username);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);


        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(messageAdapter);

        if (imageUrl.equals("default")) {
            propic.setImageResource(R.drawable.no_p);
        } else {
            Glide.with(this).load(imageUrl).into(propic);
        }

        messageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text = s.toString();
                if (text.isEmpty()) {
                    send.setImageResource(R.drawable.ic_send_hash);
                } else {
                    send.setImageResource(R.drawable.ic_send_blue);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        reference = FirebaseStorage.getInstance().getReference("SendImages");

        Query databaseReference = firebaseDatabase.getReference("Message").child(firebaseAuth.getCurrentUser().getUid()).limitToLast(size);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    if (message.getSenderId().equals(firebaseAuth.getCurrentUser().getUid()) && message.getReceiverId().equals(id)
                            || message.getSenderId().equals(id) && message.getReceiverId().equals(firebaseAuth.getCurrentUser().getUid())) {
                        messageList.add(message);
                    }
                    messageAdapter = new MessageAdapter(messageList, MessageActivity.this, imageUrl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                size = size + 50;

                Query databaseReference = firebaseDatabase.getReference("Message").child(firebaseAuth.getCurrentUser().getUid()).limitToLast(size);
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Message message = dataSnapshot.getValue(Message.class);
                            if (message.getSenderId().equals(firebaseAuth.getCurrentUser().getUid()) && message.getReceiverId().equals(id)
                                    || message.getSenderId().equals(id) && message.getReceiverId().equals(firebaseAuth.getCurrentUser().getUid())) {
                                messageList.add(message);
                            }
                            messageAdapter.setMessageListupdate(messageList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        setiseen = firebaseDatabase.getReference("Message").child(id);
        valueEventListener =  setiseen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    if (message.getSenderId().equals(firebaseAuth.getCurrentUser().getUid()) && message.getReceiverId().equals(id)
                            || message.getSenderId().equals(id) && message.getReceiverId().equals(firebaseAuth.getCurrentUser().getUid())) {
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("isSeen", "yes");
                        dataSnapshot.getRef().updateChildren(hashMap);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }




    @Override
    protected void onPause() {
        super.onPause();
        setiseen.removeEventListener(valueEventListener);
    }

    public void sendMessage(View view) {

        String getMessage = messageText.getText().toString();

        if (getMessage.isEmpty()) {
            Toast.makeText(this, "cannot send empty message !", Toast.LENGTH_SHORT).show();
            return;
        }

        HashMap<String, Object> messageMap = new HashMap<>();
        messageMap.put("senderId", firebaseAuth.getCurrentUser().getUid());
        messageMap.put("receiverId", id);
        messageMap.put("imageUrl", "default");
        messageMap.put("isSeen", "no");
        messageMap.put("message", getMessage);

        DatabaseReference databaseReference1 = firebaseDatabase.getReference("Message")
                .child(firebaseAuth.getCurrentUser().getUid());

        DatabaseReference databaseReference2 = firebaseDatabase.getReference("Message")
                .child(id);

        databaseReference1.push().setValue(messageMap);

        databaseReference2.push().setValue(messageMap);


        DatabaseReference sendLast1 = firebaseDatabase.getReference("Admins").child(id);
        DatabaseReference sendLast2 = firebaseDatabase.getReference("Users").child(firebaseAuth.getCurrentUser().getUid());

        Map<String,Object> map = new HashMap<>();
        map.put("lastMessage",getMessage);
        sendLast1.updateChildren(map);
        sendLast2.updateChildren(map);

        messageText.setText("");

    }

    public void finish(View view) {
        finish();
    }

    public void sendImage(View view) {

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//
//            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
//
//                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//            } else {
                getPhoto();
//            }
//        } else {
//            getPhoto();
//        }
    }

    public void getPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);

    }

    private String getMineType(Uri uri) {

        ContentResolver contentResolver = getContentResolver();

        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();

        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            assert data != null;

            uploadUri = data.getData();

            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setMessage("Confirm Send")
                    .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Toast.makeText(MessageActivity.this, "Sending...", Toast.LENGTH_SHORT).show();
                            doSendImage();
                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            imageUrl = null;
                        }
                    });
            alertDialog = builder.create();
            alertDialog.show();
        }

    }

    void doSendImage() {
        StorageReference storageReference = reference.child(getMineType(uploadUri) + "." + System.currentTimeMillis());
        UploadTask uploadTask = storageReference.putFile(uploadUri);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {

                    String getDownloadUrl = task.getResult().toString();

                    HashMap<String, Object> messageMap = new HashMap<>();
                    messageMap.put("senderId", firebaseAuth.getCurrentUser().getUid());
                    messageMap.put("receiverId", id);
                    messageMap.put("imageUrl", getDownloadUrl);
                    messageMap.put("isSeen", "no");
                    messageMap.put("message", "");

                    DatabaseReference databaseReference1 = firebaseDatabase.getReference("Message")
                            .child(firebaseAuth.getCurrentUser().getUid());

                    DatabaseReference databaseReference2 = firebaseDatabase.getReference("Message")
                            .child(id);

                    databaseReference1.push().setValue(messageMap);

                    databaseReference2.push().setValue(messageMap);

                    DatabaseReference sendLast1 = firebaseDatabase.getReference("Admins").child(id);
                    DatabaseReference sendLast2 = firebaseDatabase.getReference("Users").child(firebaseAuth.getCurrentUser().getUid());

                    Map<String,Object> map = new HashMap<>();
                    map.put("lastMessage","sent an image");
                    sendLast1.updateChildren(map);
                    sendLast2.updateChildren(map);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MessageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        valueEventListener =  setiseen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    if (message.getSenderId().equals(firebaseAuth.getCurrentUser().getUid()) && message.getReceiverId().equals(id)
                            || message.getSenderId().equals(id) && message.getReceiverId().equals(firebaseAuth.getCurrentUser().getUid())) {
                        HashMap<String,Object> hashMap = new HashMap<>();
                        hashMap.put("isSeen", "yes");
                        dataSnapshot.getRef().updateChildren(hashMap);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
}}
