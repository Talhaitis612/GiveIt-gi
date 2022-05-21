package com.example.giveit_gi.DonorActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.giveit_gi.R;
import com.example.giveit_gi.Utils.CONSTANTS;
import com.example.giveit_gi.Utils.LoadingBar;
import com.example.giveit_gi.Utils.PasswordEncryptionDecryption;
import com.example.giveit_gi.databinding.ActivityDonorRegisterBinding;
import com.example.giveit_gi.Models.Donor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class DonorRegisterActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityDonorRegisterBinding binding;
    //    Firebase Auth Intializiation and FireStore database
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDonorRegisterBinding.inflate(getLayoutInflater());
        getSupportActionBar().setTitle(R.string.create_new_account);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        View view = binding.getRoot();
        setContentView(view);
//      Intializing ProgressDialog
        //Intializing Firebase
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mRef = mDatabase.getReference();


        binding.createAccountButton.setOnClickListener(this);
        binding.alreadyHaveAccountBtn.setOnClickListener(this);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        startActivity(new Intent(DonorRegisterActivity.this, DonorLoginActivity.class));
        finish();
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.already_have_account_btn:
                startActivity(new Intent(this, DonorLoginActivity.class));
                finish();
                break;

            case R.id.createAccountButton:
                registerUser();
                break;
        }

    }

    //    To register a user
    private void registerUser() {
        String name = binding.nameEditText.getText().toString().trim();
        String email = binding.emailEditText.getText().toString().trim();
        String phone = binding.phoneEditText.getText().toString().trim();
        String password = binding.passwordEditText.getText().toString().trim();
        String confirmPassword = binding.confirmPasswordEditText.getText().toString().trim();
        if (name.isEmpty()) {
            binding.nameLayout.setError(getString(R.string.err_name_empty));
            binding.nameLayout.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            binding.emailEditText.setError(getString(R.string.err_email_empty));
            binding.emailEditText.requestFocus();
            return;
        }
        if (phone.isEmpty()) {
            binding.phoneLayout.setError(getString(R.string.err_phone_empty));
            binding.phoneLayout.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            binding.passwordLayout.setError(getString(R.string.err_password));
            binding.passwordLayout.requestFocus();
            return;
        }
        if (confirmPassword.isEmpty()) {
            binding.confirmPasswordLayout.setError(getString(R.string.err_password_confirm));
            binding.confirmPasswordLayout.requestFocus();
            return;
        }
        if (!password.equals(confirmPassword)) {
            binding.confirmPasswordLayout.setError(getString(R.string.err_password_match));
            binding.confirmPasswordLayout.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailLayout.setError(getString(R.string.err_email_format));
        }
        if (password.length() < 8) {
            binding.passwordLayout.setError(getString(R.string.err_password_length));
            binding.passwordLayout.requestFocus();

        } else {
            binding.nameLayout.setError(null);
            binding.emailLayout.setError(null);
            binding.phoneLayout.setError(null);
            binding.passwordLayout.setError(null);
            binding.confirmPasswordLayout.setError(null);

            LoadingBar.showLoadingBar(this, getString(R.string.create_account_title), getString(R.string.create_account_desc));
            mAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    LoadingBar.hideLoadingBar();
                    Toast.makeText(DonorRegisterActivity.this, getString(R.string.account_created_successfully), Toast.LENGTH_SHORT).show();
                    mRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Donor donor = new Donor(name,email,phone,password,"");
                            mRef.child(CONSTANTS.DONOR_COLLECTION_PATH).child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()).setValue(donor).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        startActivity(new Intent(DonorRegisterActivity.this, DonorLoginActivity.class));
                                        finish();


                                    }
                                    else {
                                        Toast.makeText(DonorRegisterActivity.this, "Something went wrong, please try again later.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    LoadingBar.hideLoadingBar();
                    Toast.makeText(DonorRegisterActivity.this, "Error:" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}