package com.eflexsoft.sorightadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.eflexsoft.sorightadmin.fragments.HomeFragment;
import com.eflexsoft.sorightadmin.fragments.MessageFragment;
import com.eflexsoft.sorightadmin.fragments.MyBookingFragment;
import com.eflexsoft.sorightadmin.fragments.NotificationFragment;
import com.eflexsoft.sorightadmin.fragments.UploadFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import dagger.android.support.DaggerAppCompatActivity;

public class MainActivity extends DaggerAppCompatActivity {

    FrameLayout frameLayout;
    Fragment fragment;
    BottomNavigationView bottomNavigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.botton_nav);
        toolbar = findViewById(R.id.toolbar);
        frameLayout = findViewById(R.id.main_frame);
        setSupportActionBar(toolbar);


        if (savedInstanceState == null){
            setTitle("Home");
            fragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,fragment).commit();
        }

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.home:
                        toolbar.setTitle("Home");
                        fragment = new HomeFragment();
                        break;

                    case R.id.upload:
                        toolbar.setTitle("Upload");
                        fragment = new UploadFragment();
                        break;

                    case R.id.book:
                        toolbar.setTitle("My Bookings");
                        fragment = new MyBookingFragment();
                        break;

//                    case R.id.notification:
//                        toolbar.setTitle("Notifications");
//                        fragment = new NotificationFragment();
//                        break;

                    case R.id.messages:
                        toolbar.setTitle("Messages");
                        fragment = new MessageFragment();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.main_frame,fragment).commit();
                return true;
            }
        });

    }
}
