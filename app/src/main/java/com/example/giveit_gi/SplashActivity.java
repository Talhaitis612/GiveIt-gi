package com.example.giveit_gi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.giveit_gi.DonorActivities.DonorHomeActivity;
import com.example.giveit_gi.Utils.CONSTANTS;
import com.example.giveit_gi.databinding.ActivitySplashBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
        super.onStart();
        Paper.init(this);
        String userEmail = Paper.book().read(CONSTANTS.USER_EMAIL_KEY);
        String userPassword = Paper.book().read(CONSTANTS.USER_PASSWORD_KEY);
        if (!Objects.isNull(userEmail) && !Objects.isNull(userPassword)) {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            assert userEmail != null;
            assert userPassword != null;

            mAuth.signInWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    startActivity(new Intent(SplashActivity.this, DonorHomeActivity.class));
                }
            });


        } else {
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();

        }


    }

}