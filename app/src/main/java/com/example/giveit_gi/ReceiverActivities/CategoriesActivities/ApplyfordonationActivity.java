package com.example.giveit_gi.ReceiverActivities.CategoriesActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.giveit_gi.Models.ApplyDonation;
import com.example.giveit_gi.Models.RequestDonation;
import com.example.giveit_gi.R;
import com.example.giveit_gi.ReceiverActivities.CRUD.ItemFormActivity;
import com.example.giveit_gi.Utils.CONSTANTS;
import com.example.giveit_gi.Utils.LoadingBar;
import com.example.giveit_gi.databinding.ActivityApplyfordonationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class ApplyfordonationActivity extends AppCompatActivity {

    private ActivityApplyfordonationBinding binding;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityApplyfordonationBinding.inflate(getLayoutInflater());
        getSupportActionBar().setTitle("Apply for donation");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(binding.getRoot());
        db= FirebaseFirestore.getInstance();

        binding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitApplication();
            }
        });

    }

    private void submitApplication() {
        String title = binding.titleEditText.getText().toString().trim();
        String name = binding.nameEditText.getText().toString().trim();
        String email = binding.emailEditText.getText().toString().trim();
        String phone = binding.phoneEditText.getText().toString().trim();
        String idCardNumber = binding.idCardEditText.getText().toString().trim();
        String location = binding.locationEditText.getText().toString().trim();
        String problem = binding.problemEditText.getText().toString().trim();
        String amount = binding.amountEditText.getText().toString();


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
        if (TextUtils.isEmpty(binding.problemEditText.getText())) {
            binding.locationLayout.setError(getString(R.string.problem_cannot_be_null));

        }
        if (TextUtils.isEmpty(binding.amountEditText.getText())) {
            binding.locationLayout.setError(getString(R.string.amount_cannot_be_null));

        }
        if (TextUtils.isEmpty(binding.titleEditText.getText())) {
            binding.nameLayout.setError(getString(R.string.err_title_empty));
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailLayout.setError(getString(R.string.err_email_format));
        }
        else {
            LoadingBar.showLoadingBar(this, getString(R.string.processing_request), getString(R.string.please_wait_request));
            String generateUniqueRequestID = CONSTANTS.generateUniqueKey(30);

            ApplyDonation appliedDonation = new ApplyDonation(
                    generateUniqueRequestID,
                    name,
                    email,
                    idCardNumber,
                    phone,
                    title,
                    problem,
                    location,
                    amount,
                    0,
                    false,
                    Calendar.getInstance().getTime()
            );
//            One request per ID CARD
            db.collection(CONSTANTS.APPLY_ITEM_COLLECTION_PATH)
                    .document(idCardNumber)
                    .set(appliedDonation).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                LoadingBar.hideLoadingBar();
                                Toast.makeText(ApplyfordonationActivity.this, "Your request has been submitted successfully!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                            else {
                                Toast.makeText(ApplyfordonationActivity.this, "Something went wrong try again later.", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }




}