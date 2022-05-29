package com.example.giveit_gi.DonorActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.giveit_gi.R;
import com.example.giveit_gi.databinding.ActivityDonateThingsBinding;

public class DonateThingsActivity extends AppCompatActivity {
    private ActivityDonateThingsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDonateThingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}