package com.example.giveit_gi.DonorActivities.CategoriesActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.giveit_gi.databinding.ActivityEventBinding;

public class EventActivity extends AppCompatActivity {

    ActivityEventBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());






    }
}