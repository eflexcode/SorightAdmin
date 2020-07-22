package com.eflexsoft.sorightadmin.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eflexsoft.sorightadmin.R;
import com.eflexsoft.sorightadmin.adapters.ChoseAdapter;
import com.eflexsoft.sorightadmin.model.ChoseItem;

import java.util.ArrayList;
import java.util.List;

public class UploadFragment extends Fragment {

    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this

        View view = inflater.inflate(R.layout.fragment_upload, container, false);

        recyclerView = view.findViewById(R.id.chose_upload_recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext() ,2));

        List<ChoseItem> choseItemList = new ArrayList<>();
        choseItemList.add(new ChoseItem(R.color.colorPrimary,"White Wedding"));
        choseItemList.add(new ChoseItem(R.color.colorPrimaryDark,"Traditional Wedding"));
        choseItemList.add(new ChoseItem(R.color.anniversary,"Anniversary"));
        choseItemList.add(new ChoseItem(R.color.birthday,"Birthday"));
        choseItemList.add(new ChoseItem(R.color.beria,"Burial"));
        choseItemList.add(new ChoseItem(R.color.others,"Others"));

        recyclerView.setAdapter(new ChoseAdapter(choseItemList,getContext()));

        return view;
    }
}
