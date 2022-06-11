package com.example.giveit_gi.DonorActivities.CategoriesActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.giveit_gi.databinding.ActivityDonateMoneyBinding;

public class DonateMoneyActivity extends AppCompatActivity {

    private ActivityDonateMoneyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDonateMoneyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Donate Money");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}