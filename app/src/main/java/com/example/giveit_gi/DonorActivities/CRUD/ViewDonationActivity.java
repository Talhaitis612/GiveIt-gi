package com.example.giveit_gi.DonorActivities.CRUD;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.giveit_gi.DonorActivities.Interfaces.ItemClickListener;
import com.example.giveit_gi.Utils.CONSTANTS;
import com.example.giveit_gi.databinding.ActivityUpdateDonationBinding;
import com.example.giveit_gi.databinding.ActivityViewDonationBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class ViewDonationActivity extends AppCompatActivity  {
    private ActivityViewDonationBinding binding;
    private String donorID,donationID,title,description,category, location, imageURL;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewDonationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();


        Intent intent = getIntent();
        donorID = intent.getStringExtra(CONSTANTS.DONOR_ID);
        donationID = intent.getStringExtra(CONSTANTS.DONATION_ID);
        title = intent.getStringExtra(CONSTANTS.DONATION_TITLE);
        description = intent.getStringExtra(CONSTANTS.DONATION_DESCRIPTION);
        category = intent.getStringExtra(CONSTANTS.DONATION_CATEGORY);
        location = intent.getStringExtra(CONSTANTS.DONATION_LOCATION);
        imageURL = intent.getStringExtra(CONSTANTS.DONATION_IMAGE_URL);
        if(Objects.equals(donorID, mAuth.getCurrentUser().getUid()))
        {
            binding.buttonLayout.setVisibility(View.VISIBLE);
        }
        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        binding.donationTitle.setText(title);
        binding.donationDescription.setText(description);
        binding.donationCategory.setText(category);
        binding.donationLocation.setText(location);
        Glide.with(this).load(imageURL).into(binding.heroImage);


    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}