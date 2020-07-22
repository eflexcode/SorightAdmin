package com.eflexsoft.sorightadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.eflexsoft.sorightadmin.model.OrderList;
import com.eflexsoft.sorightadmin.model.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class OrderDetailsActivity extends DaggerAppCompatActivity {

    Intent intent;
    TextView orderName;
    TextView orderEmail;
    TextView orderPhone;
    TextView orderSdate;
    TextView orderId;
    TextView orderDdate;
    TextView orderPrice;
    AlertDialog alertDialog;

    String id;
    String statDate;

    @Inject
    FirebaseDatabase firebaseDatabase;

    @Inject
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        setContentView(R.layout.activity_order_details);

        intent = getIntent();

        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String phone = intent.getStringExtra("Phone");
        String statDate = intent.getStringExtra("Sdate");
        String endDate = intent.getStringExtra("Edate");
        String id = intent.getStringExtra("id");
        String price = intent.getStringExtra("price");

        this.id = intent.getStringExtra("idforDelete");
        this.statDate = intent.getStringExtra("date");

        orderName = findViewById(R.id.name);
        orderEmail = findViewById(R.id.email);
        orderPhone = findViewById(R.id.phone);
        orderSdate = findViewById(R.id.start);
        orderId = findViewById(R.id.id);
        orderPrice = findViewById(R.id.Package);
        orderDdate = findViewById(R.id.end);

        orderName.setText(name);
        orderDdate.setText(endDate);
        orderSdate.setText(statDate);
        orderEmail.setText(email);
        orderPrice.setText(price);
        orderPhone.setText(phone);
        orderId.setText(id);

    }

    public void takeOrder(View view) {

        DatabaseReference databaseReference = firebaseDatabase.getReference("Orders");
        DatabaseReference databaseReferenceNotify = firebaseDatabase.getReference("Notify").child(id);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Sending notification to user");

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setMessage("Have you write'n down the contact of this order because the order will be delete and a message will be sent to the user ")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        progressDialog.show();

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("text", "Your order have taken");

                        HashMap<String, Object> messageMap = new HashMap<>();
                        messageMap.put("senderId", firebaseAuth.getCurrentUser().getUid());
                        messageMap.put("receiverId", id);
                        messageMap.put("imageUrl", "default");
                        messageMap.put("isSeen", "no");
                        messageMap.put("message", "your order have been taken see inbox");

                        DatabaseReference databaseReference1 = firebaseDatabase.getReference("Message")
                                .child(firebaseAuth.getCurrentUser().getUid());

                        DatabaseReference databaseReference2 = firebaseDatabase.getReference("Message")
                                .child(id);

                        databaseReference1.push().setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.setMessage("messaging user");
                                }
                            }
                        });

                        databaseReference2.push().setValue(messageMap);

                        databaseReferenceNotify.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    progressDialog.setMessage("deleting order");
                                    databaseReference.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                OrderList orderList = dataSnapshot.getValue(OrderList.class);
                                                if (orderList.getStartDate().equals(statDate)) {
                                                    dataSnapshot.getRef().setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                progressDialog.dismiss();
                                                                finish();
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                                }
                            }
                        });


                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog = builder.create();
        alertDialog.show();


    }
}