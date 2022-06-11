package com.example.giveit_gi.ReceiverActivities.CategoriesActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.giveit_gi.databinding.ActivityApplyfordonationBinding;

public class ApplyfordonationActivity extends AppCompatActivity {

    private ActivityApplyfordonationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityApplyfordonationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}