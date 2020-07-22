package com.eflexsoft.sorightadmin.di;

import android.app.Application;

import com.eflexsoft.sorightadmin.ApplicationMain;
import com.eflexsoft.sorightadmin.di.firebasemodels.FirebaseModel;
import com.eflexsoft.sorightadmin.di.ui.ActivityFragmentModel;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(modules = {
        AndroidSupportInjectionModule.class,
        FirebaseModel.class,
        ActivityFragmentModel.class
})
public interface MainComponent extends AndroidInjector<ApplicationMain> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        Builder builder(Application application);

        MainComponent mainComponent();

    }

}
