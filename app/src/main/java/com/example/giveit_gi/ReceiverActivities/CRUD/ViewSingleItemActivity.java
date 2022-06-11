package com.example.giveit_gi.ReceiverActivities.CRUD;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.example.giveit_gi.Utils.CONSTANTS;
import com.example.giveit_gi.databinding.ActivityItemListBinding;
import com.example.giveit_gi.databinding.ActivityViewItemBinding;

public class ViewSingleItemActivity extends AppCompatActivity {

    ActivityViewItemBinding binding;
    private String donorID,donationID,title,description,category, location, imageURL, donationTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewItemBinding.inflate(getLayoutInflater());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        View view = binding.getRoot();
        setContentView(view);
        Intent intent = getIntent();
        donorID = intent.getStringExtra(CONSTANTS.DONOR_ID);
        donationID = intent.getStringExtra(CONSTANTS.DONATION_ID);
        title = intent.getStringExtra(CONSTANTS.DONATION_TITLE);
        description = intent.getStringExtra(CONSTANTS.DONATION_DESCRIPTION);
        category = intent.getStringExtra(CONSTANTS.DONATION_CATEGORY);
        location = intent.getStringExtra(CONSTANTS.DONATION_LOCATION);
        imageURL = intent.getStringExtra(CONSTANTS.DONATION_IMAGE_URL);
        donationTime = intent.getStringExtra(CONSTANTS.DONATION_TIME);

        getSupportActionBar().setTitle(title);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        binding.donationTitle.setText(title);
        binding.donationDescription.setText(description);
        binding.donationCategory.setText(category);
        binding.donationLocation.setText(location);
        binding.donationTime.setText(donationTime);
        Glide.with(this).load(imageURL).into(binding.heroImage);

        binding.getitemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewSingleItemActivity.this, ItemFormActivity.class);
                intent.putExtra(CONSTANTS.DONATION_ID, donationID);
                startActivity(intent);
            }
        });




    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}