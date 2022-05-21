package com.example.giveit_gi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.giveit_gi.DonorActivities.DonorHomeActivity;
import com.example.giveit_gi.Utils.Prevalent;
import com.example.giveit_gi.databinding.ActivitySplashBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


import java.util.Objects;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {
    ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Paper.init(this);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide_animation);
        binding.splashScreen.startAnimation(animation);
        //Splash Screen duration
        int secondsDelayed = 1;
        new Handler().postDelayed(new Runnable() {
            public void run() {

            }
        }, secondsDelayed * 3000);
    }

    @Override
    protected void onStart() {

        Paper.init(this);
        String userEmail = Paper.book().read(Prevalent.USER_EMAIL_KEY);
        String userPassword = Paper.book().read(Prevalent.USER_PASSWORD_KEY);
        if (!Objects.isNull(userEmail) && !Objects.isNull(userPassword))
        {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            assert userEmail != null;
            assert userPassword != null;
            mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    startActivity(new Intent(SplashActivity.this, DonorHomeActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            });
            Prevalent.retrieveUserInformation(mAuth.getCurrentUser().getUid());
        }
        else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();

        }

        super.onStart();
    }



    }