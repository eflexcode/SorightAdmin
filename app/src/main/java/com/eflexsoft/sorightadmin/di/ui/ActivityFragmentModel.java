package com.eflexsoft.sorightadmin.di.ui;

import com.eflexsoft.sorightadmin.FullImageActivity;
import com.eflexsoft.sorightadmin.LoginActivity;
import com.eflexsoft.sorightadmin.MainActivity;
import com.eflexsoft.sorightadmin.OrderDetailsActivity;
import com.eflexsoft.sorightadmin.SignUpActivity;
import com.eflexsoft.sorightadmin.SplashActivity;
import com.eflexsoft.sorightadmin.UploadActivity;
import com.eflexsoft.sorightadmin.fragments.HomeFragment;
import com.eflexsoft.sorightadmin.fragments.MyBookingFragment;
import com.eflexsoft.sorightadmin.fragments.UploadFragment;

import dagger.Binds;
import dagger.BindsInstance;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityFragmentModel {

    @ContributesAndroidInjector
    abstract MainActivity mainActivity();

    @ContributesAndroidInjector
    abstract LoginActivity loginActivity();

    @ContributesAndroidInjector
    abstract SignUpActivity signUpActivity();

    @ContributesAndroidInjector
    abstract SplashActivity splashActivity();

    @ContributesAndroidInjector
    abstract UploadActivity uploadActivity();

    @ContributesAndroidInjector
    abstract HomeFragment homeFragment();

    @ContributesAndroidInjector
    abstract FullImageActivity fullImageActivity();

    @ContributesAndroidInjector
    abstract MyBookingFragment myBookingFragment();

    @ContributesAndroidInjector
    abstract OrderDetailsActivity orderDetailsActivity();

}
