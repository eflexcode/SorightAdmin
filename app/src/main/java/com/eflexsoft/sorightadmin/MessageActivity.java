package com.eflexsoft.sorightadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class MessageActivity extends DaggerAppCompatActivity {

    EditText messageText;
    ImageButton send;
    CircleImageView propic;
    TextView name;


    @Inject
    FirebaseAuth firebaseAuth;

    @Inject
    FirebaseDatabase firebaseDatabase;

    String id;
    Intent intent;
    String username;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorAccent));
        setContentView(R.layout.activity_message);

        messageText = findViewById(R.id.messageText);
        send = findViewById(R.id.send);
        name = findViewById(R.id.username);
        propic = findViewById(R.id.Pro_pic);

        intent = getIntent();
        id = intent.getStringExtra("id");
        username = intent.getStringExtra("name");
        imageUrl = intent.getStringExtra("image");

        name.setText(username);

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

        messageText.setText("");

    }

    public void finish(View view) {
        finish();
    }
}