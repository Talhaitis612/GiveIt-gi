package com.example.giveit_gi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.giveit_gi.DonorActivities.DonorHomeActivity;
import com.example.giveit_gi.DonorActivities.DonorLoginActivity;
import com.example.giveit_gi.Models.Donor;
import com.example.giveit_gi.ReceiverActivities.ReceiverActivity;
import com.example.giveit_gi.Utils.CONSTANTS;
import com.example.giveit_gi.Utils.Prevalent;
import com.example.giveit_gi.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {
    //    Using binding to reduce the boilerplate code and make the application more readable
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

            binding.donorButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, DonorLoginActivity.class));
                    finish();
                }
            });

            binding.receiverButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this, ReceiverActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

        }




    @Override
    protected void onStart() {
        super.onStart();

    }
}