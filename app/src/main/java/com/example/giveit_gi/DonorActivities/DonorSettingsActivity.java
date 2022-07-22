package com.example.giveit_gi.DonorActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.giveit_gi.R;
import com.example.giveit_gi.databinding.ActivityDonorSettingsBinding;

public class DonorSettingsActivity extends AppCompatActivity {
    ActivityDonorSettingsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Reading the layout file
        binding = ActivityDonorSettingsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();

        setContentView(view);

    }
}