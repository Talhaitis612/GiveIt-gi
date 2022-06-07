package com.example.giveit_gi.DonorActivities.AuthenticationActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.giveit_gi.DonorActivities.DonorHomeActivity;
import com.example.giveit_gi.R;
import com.example.giveit_gi.Utils.CONSTANTS;
import com.example.giveit_gi.Utils.LoadingBar;
import com.example.giveit_gi.databinding.ActivityDonorLoginBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import io.paperdb.Paper;

public class DonorLoginActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityDonorLoginBinding binding;
    //    FirebaseAuth for Authentication
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDonorLoginBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();



        Paper.init(this);




//        Registering all click listeners

        binding.createNewAccountBtn.setOnClickListener(this);
        binding.loginButton.setOnClickListener(this);
        binding.forgotPasswordButton.setOnClickListener(this);

        binding.emailEdittext.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (binding.emailEdittext.getText().toString().isEmpty()) {
                    binding.emailEdittextLayout.setError(null);
                }
                return false;
            }
        });
        binding.passwordEdittext.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (binding.passwordEdittext.getText().toString().isEmpty()) {
                    binding.passwordEdittextLayout.setError(null);
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginButton:
                userLogin();
                break;
            case R.id.create_new_account_btn:
                startActivity(new Intent(this, DonorRegisterActivity.class));
                finish();
                break;
            case R.id.forgotPasswordButton:
                forgotPassword(view);
                break;


        }
    }

    private void userLogin() {
        if(binding.remmberMeCheckbox.isChecked()){
        }
        String email = binding.emailEdittext.getText().toString().trim();
        String password = binding.passwordEdittext.getText().toString().trim();
        if (email.isEmpty()) {
            binding.passwordEdittextLayout.setError(getString(R.string.err_password));
        }
        if (password.isEmpty()) {
            binding.emailEdittextLayout.setError(getString(R.string.err_email_empty));
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailEdittextLayout.setError(getString(R.string.err_email_format));

        }
        else {
            binding.passwordEdittextLayout.setError(null);
            binding.emailEdittextLayout.setError(null);
            LoadingBar.showLoadingBar(this, getString(R.string.login_title), getString(R.string.loggin_description));

//           PasswordEncryptionDecryption.decryptPassword(password)
            mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    if (binding.remmberMeCheckbox.isChecked()){
                        Paper.book().write(CONSTANTS.USER_EMAIL_KEY, email);
                        Paper.book().write(CONSTANTS.USER_PASSWORD_KEY, password);
                    }

                    startActivity(new Intent(DonorLoginActivity.this, DonorHomeActivity.class));
                    finish();
                    Toast.makeText(DonorLoginActivity.this, "LoggedIn Successfully!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    LoadingBar.hideLoadingBar();
                    Toast.makeText(DonorLoginActivity.this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }


    void forgotPassword(View view) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_layout, null);
        dialog.setView(dialogView);
        TextInputEditText resetEmailEditText = (TextInputEditText) dialogView.findViewById(R.id.reset_email_edittext);
        TextInputLayout resetEmailLayout = dialogView.findViewById(R.id.reset_email_edittext_layout);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);
        Button confirmButton = dialogView.findViewById(R.id.confirmButton);
        AlertDialog alertDialog = dialog.create();
        alertDialog.show();


        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = resetEmailEditText.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    resetEmailLayout.setError("Please enter email...");
                } else {
                    LoadingBar.showLoadingBar(view.getContext(), "Wait..", getString(R.string.loggin_description));
                    mAuth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            LoadingBar.hideLoadingBar();
                            alertDialog.dismiss();
                            Toast.makeText(DonorLoginActivity.this, "Reset link sent to your email.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            LoadingBar.hideLoadingBar();
                            alertDialog.dismiss();
                            Toast.makeText(DonorLoginActivity.this, "Failed to sent the link." + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });


    }


}