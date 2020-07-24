package com.eflexsoft.sorightadmin.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eflexsoft.sorightadmin.R;
import com.eflexsoft.sorightadmin.adapters.ChatLisAdapter;
import com.eflexsoft.sorightadmin.model.ChatList;
import com.eflexsoft.sorightadmin.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;


public class MessageFragment extends DaggerFragment {

    RecyclerView recyclerView;

    @Inject
    FirebaseDatabase firebaseDatabase;

    @Inject
    FirebaseAuth firebaseAuth;

    AVLoadingIndicatorView avLoadingIndicatorView;

    List<ChatList> chatListList = new ArrayList<>();
    List<User> userList = new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            DatabaseReference databaseReference = firebaseDatabase.getReference("ChatList")
                    .child(firebaseAuth.getCurrentUser().getUid());
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    chatListList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        ChatList chatList = dataSnapshot.getValue(ChatList.class);
                        chatListList.add(chatList);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(), "your not logged in", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message, container, false);

        recyclerView = view.findViewById(R.id.messageRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ChatLisAdapter chatLisAdapter = new ChatLisAdapter(getContext());
        recyclerView.setAdapter(chatLisAdapter);

        avLoadingIndicatorView = view.findViewById(R.id.progress);
        avLoadingIndicatorView.setVisibility(View.VISIBLE);

        try {
            DatabaseReference userReference = firebaseDatabase.getReference("Users");
            userReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    userList.clear();
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                        User user = dataSnapshot.getValue(User.class);
                        for (ChatList chatList : chatListList){
                            assert user != null;
                            if (chatList.getId().equals(user.getId())) {
                                userList.add(user);
                            }
                            chatLisAdapter.setUserList(userList);
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(), "your not logged in", Toast.LENGTH_SHORT).show();
        }


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Thread.sleep(5000);
                    avLoadingIndicatorView.setVisibility(View.VISIBLE);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                avLoadingIndicatorView.setVisibility(View.GONE);


            }

        }.execute();

        return view;
    }
}
