package com.example.giveit_gi.ReceiverActivities.CRUD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.giveit_gi.Models.Donation;
import com.example.giveit_gi.Models.RequestDonation;
import com.example.giveit_gi.R;
import com.example.giveit_gi.Utils.CONSTANTS;
import com.example.giveit_gi.Utils.LoadingBar;
import com.example.giveit_gi.databinding.ActivityItemFormBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ItemFormActivity extends AppCompatActivity {

    ActivityItemFormBinding binding;

    //    FOR THE 30 DAYS CONSTRAINTS
    Calendar calendar = Calendar.getInstance();
    private FirebaseFirestore db;
    private String donationID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding = ActivityItemFormBinding.inflate(getLayoutInflater());
        calendar.add(Calendar.DATE, 30);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Get this item");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.d("30 DAYS", calendar.toString());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        donationID = intent.getStringExtra(CONSTANTS.DONATION_ID);

        db = FirebaseFirestore.getInstance();

        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestItemDonation();
            }
        });


    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }


    private void requestItemDonation() {
        String name = binding.nameEditText.getText().toString().trim();
        String email = binding.emailEditText.getText().toString().trim();
        String phone = binding.phoneEditText.getText().toString().trim();
        String idCardNumber = binding.idCardEditText.getText().toString().trim();
        String location = binding.locationEditText.getText().toString().trim();
        String reason = binding.reasonEditText.getText().toString().trim();


        if (TextUtils.isEmpty(binding.nameEditText.getText())) {
            binding.nameLayout.setError(getString(R.string.err_name_empty));
        }
        if (TextUtils.isEmpty(binding.emailEditText.getText())) {
            binding.emailLayout.setError(getString(R.string.err_email_empty));
        }
        if (TextUtils.isEmpty(binding.phoneEditText.getText())) {
            binding.phoneLayout.setError(getString(R.string.err_phone_empty));

        }
        if (TextUtils.isEmpty(binding.idCardEditText.getText())) {
            binding.idCardLayout.setError(getString(R.string.id_card_important));

        }
        if (TextUtils.isEmpty(binding.locationEditText.getText())) {
            binding.locationLayout.setError(getString(R.string.location_cannot_be_null));

        }
        if (TextUtils.isEmpty(binding.reasonEditText.getText())) {
            binding.locationLayout.setError(getString(R.string.reason_cannot_be_null));

        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailLayout.setError(getString(R.string.err_email_format));
        } else {
            LoadingBar.showLoadingBar(this, getString(R.string.processing_request), getString(R.string.please_wait_request));
            String generateUniqueRequestID = CONSTANTS.generateUniqueKey(30);

            RequestDonation requestDonation = new RequestDonation(
                    generateUniqueRequestID,
                    donationID,
                    name,
                    email,
                    idCardNumber,
                    phone,
                    location,
                    reason,
                    false,
                    Calendar.getInstance().getTime()
            );
//            One request per ID CARD
            db.collection(CONSTANTS.REQUEST_ITEM_COLLECTION_PATH)
                    .document(idCardNumber+"and"+donationID)
                    .set(requestDonation).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            LoadingBar.hideLoadingBar();
                            Toast.makeText(ItemFormActivity.this, "Your request has been submitted successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
        }
    }
}