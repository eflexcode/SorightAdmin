package com.eflexsoft.sorightadmin.di.firebasemodels;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import dagger.Component;
import dagger.Module;
import dagger.Provides;

@Module
public abstract class FirebaseModel {

    @Provides
   static FirebaseAuth firebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    static FirebaseDatabase firebaseDatabase() {
        return FirebaseDatabase.getInstance();
    }

    @Provides
    static FirebaseStorage firebaseStorage(){
        return FirebaseStorage.getInstance();
    }

}
