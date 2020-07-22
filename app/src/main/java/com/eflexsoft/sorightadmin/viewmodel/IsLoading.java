package com.eflexsoft.sorightadmin.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class IsLoading extends AndroidViewModel {

    public MutableLiveData<Boolean> booleanMutableLiveData = new MutableLiveData<>();

    public IsLoading(@NonNull Application application) {
        super(application);
    }


    public LiveData<Boolean> booleanLiveData(){
        return booleanMutableLiveData;
    }
}
