package com.example.giveit_gi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.giveit_gi.DonorActivities.DonorLoginActivity;
import com.example.giveit_gi.ReceiverActivities.ReceiverActivity;
import com.example.giveit_gi.databinding.ActivityMainBinding;

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
}