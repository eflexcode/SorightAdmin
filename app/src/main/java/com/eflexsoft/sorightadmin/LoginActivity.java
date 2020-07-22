package com.eflexsoft.sorightadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.jgabrielfreitas.core.BlurImageView;
import com.rengwuxian.materialedittext.MaterialEditText;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class LoginActivity extends DaggerAppCompatActivity {

    MaterialEditText email;
    MaterialEditText password;
    ProgressBar progressBar;

    @Inject
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.parseColor("#00C853"));
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progress);

        BlurImageView blurImageView = findViewById(R.id.back_image);
        blurImageView.setBlur(2);
        blurImageView.setBitmapScale(1.5f);

    }

    public void doLogin(View view) {

        String getEmail = email.getText().toString();
        String getPassword = password.getText().toString();


        if (getEmail.isEmpty() || getPassword.isEmpty()) {
            Toast.makeText(this, "password or email is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (getPassword.length() < 8) {
            Toast.makeText(this, "password to short", Toast.LENGTH_SHORT).show();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.signInWithEmailAndPassword(getEmail, getPassword).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
        });

    }
}
