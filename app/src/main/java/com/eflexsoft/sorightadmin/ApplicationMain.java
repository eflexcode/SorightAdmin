package com.eflexsoft.sorightadmin;

import com.eflexsoft.sorightadmin.di.DaggerMainComponent;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;

public class ApplicationMain extends DaggerApplication {
    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        return DaggerMainComponent.builder().builder(this).mainComponent();
    }
}
