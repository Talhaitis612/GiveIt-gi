package com.example.giveit_gi.DonorActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.giveit_gi.R;
import com.example.giveit_gi.databinding.ActivityDonateThingsBinding;

import java.util.Objects;

public class DonateThingsActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityDonateThingsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDonateThingsBinding.inflate(getLayoutInflater());
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.donate_a_thing);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(binding.getRoot());

        binding.addImage.setOnClickListener(this);



    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

    }
}