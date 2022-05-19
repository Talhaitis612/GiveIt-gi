package com.example.giveit_gi.ReceiverActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.example.giveit_gi.databinding.ActivityReceiverBinding;

import java.util.Objects;

public class ReceiverActivity extends AppCompatActivity {
    private ActivityReceiverBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityReceiverBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Categories");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }
}