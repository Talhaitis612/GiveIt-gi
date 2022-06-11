package com.example.giveit_gi.ReceiverActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.giveit_gi.R;
import com.example.giveit_gi.ReceiverActivities.CategoriesActivities.ApplyfordonationActivity;
import com.example.giveit_gi.ReceiverActivities.CategoriesActivities.ItemListActivity;
import com.example.giveit_gi.databinding.ActivityReceiverBinding;

import java.util.Objects;

public class ReceiverActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityReceiverBinding binding;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityReceiverBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Categories");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.applyForDonation.setOnClickListener(this);
        binding.getAThing.setOnClickListener(this);
        binding.seeEvent.setOnClickListener(this);
        binding.suggestionBox.setOnClickListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        finish();
        return true;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.get_a_thing){

            startActivity(new Intent(ReceiverActivity.this, ItemListActivity.class));
        }
        if(id == R.id.apply_for_donation){
            startActivity(new Intent(ReceiverActivity.this, ApplyfordonationActivity.class));

        }


    }
}