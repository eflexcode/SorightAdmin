package com.eflexsoft.sorightadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jgabrielfreitas.core.BlurImageView;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class SignUpActivity extends DaggerAppCompatActivity {

    MaterialEditText username;
    MaterialEditText email;
    MaterialEditText password;
    ProgressBar progressBar;

    @Inject
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.parseColor("#00C853"));
        setContentView(R.layout.activity_sign_up);

        BlurImageView blurImageView = findViewById(R.id.back_image);
        blurImageView.setBlur(2);
        blurImageView.setBitmapScale(1.5f);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        progressBar = findViewById(R.id.progress);

    }

    public void opensignUP(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void doSignUp(View view) {
        String getUsername = username.getText().toString();
        String getEmail = email.getText().toString();
        String getPassword = password.getText().toString();


        if (getEmail.isEmpty() || getUsername.isEmpty() || getPassword.isEmpty()) {
            Toast.makeText(this, "username ore password or email is empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if (getPassword.length() < 8) {
            Toast.makeText(this, "password to short", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        firebaseAuth.createUserWithEmailAndPassword(getEmail, getPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Admins")
                            .child(firebaseAuth.getCurrentUser().getUid());

                    Map<String, Object> map = new HashMap<>();
                    map.put("id", FirebaseAuth.getInstance().getUid());
                    map.put("name", getUsername);
                    map.put("imageUrl", "default");

                    databaseReference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                startActivity(new Intent(SignUpActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}
