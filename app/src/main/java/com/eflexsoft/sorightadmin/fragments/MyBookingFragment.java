package com.eflexsoft.sorightadmin.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.eflexsoft.sorightadmin.R;
import com.eflexsoft.sorightadmin.adapters.OrderAdapter;
import com.eflexsoft.sorightadmin.model.OrderList;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class MyBookingFragment extends DaggerFragment {

    RecyclerView recyclerView;
    ArrayList<OrderList> orderListArrayList = new ArrayList<>();

    @Inject
    FirebaseDatabase firebaseDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_booking, container, false);

        recyclerView = view.findViewById(R.id.orderRecyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        OrderAdapter orderAdapter = new OrderAdapter(getContext());
        recyclerView.setAdapter(orderAdapter);

        DatabaseReference databaseReference = firebaseDatabase.getReference("Orders");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orderListArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    OrderList orderList = dataSnapshot.getValue(OrderList.class);
                    orderListArrayList.add(orderList);
                }
                orderAdapter.setOrderLists(orderListArrayList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }
}
