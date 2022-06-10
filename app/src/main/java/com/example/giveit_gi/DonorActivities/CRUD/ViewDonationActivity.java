package com.example.giveit_gi.DonorActivities.CRUD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.giveit_gi.DonorActivities.Interfaces.ItemClickListener;
import com.example.giveit_gi.R;
import com.example.giveit_gi.Utils.CONSTANTS;
import com.example.giveit_gi.databinding.ActivityUpdateDonationBinding;
import com.example.giveit_gi.databinding.ActivityViewDonationBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ViewDonationActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityViewDonationBinding binding;
    private String donorID,donationID,title,description,category, location, imageURL, donationTime;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;



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
        donationTime = intent.getStringExtra(CONSTANTS.DONATION_TIME);

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
        binding.donationTime.setText(donationTime);
        Glide.with(this).load(imageURL).into(binding.heroImage);

        binding.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewDonationActivity.this, UpdateDonationActivity.class);
                intent.putExtra(CONSTANTS.DONOR_ID, donorID);
                intent.putExtra(CONSTANTS.DONATION_ID, donationID);
                intent.putExtra(CONSTANTS.DONATION_TITLE, title);
                intent.putExtra(CONSTANTS.DONATION_DESCRIPTION, description);
                intent.putExtra(CONSTANTS.DONATION_CATEGORY, category);
                intent.putExtra(CONSTANTS.DONATION_LOCATION, location);
                intent.putExtra(CONSTANTS.DONATION_IMAGE_URL, imageURL);

                startActivity(intent);
            }
        });


        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewDonationActivity.this);
                builder.setMessage(getResources().getString(R.string.do_you_really_want_to_delete));
                builder.setTitle(getResources().getString(R.string.delete));
                builder.setCancelable(false);
                builder.setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton(getResources().getString(R.string.sure), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        db = FirebaseFirestore.getInstance();

                        db.collection(CONSTANTS.DONATION_COLLECTION_PATH)
                                .document(donationID)
                                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        DocumentReference donorRef = db.collection(CONSTANTS.DONOR_COLLECTION_PATH).document(CONSTANTS.currentloggedInDonor.getUid());
                                        donorRef.update("donationList", FieldValue.arrayRemove(donationID)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(ViewDonationActivity.this, "Donation Deleted Successfully!", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ViewDonationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });



                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View view) {

    }
}