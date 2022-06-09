package com.example.giveit_gi.DonorActivities.CRUD;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.giveit_gi.DonorActivities.CategoriesActivities.DonateThingsActivity;
import com.example.giveit_gi.DonorActivities.DonorHomeActivity;
import com.example.giveit_gi.Models.Donation;
import com.example.giveit_gi.R;
import com.example.giveit_gi.Utils.CONSTANTS;
import com.example.giveit_gi.Utils.LoadingBar;
import com.example.giveit_gi.databinding.ActivityDonateThingsBinding;
import com.example.giveit_gi.databinding.ActivityUpdateDonationBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;

public class UpdateDonationActivity extends AppCompatActivity  {
    private ActivityUpdateDonationBinding binding;
    private FirebaseFirestore db;
    private String donorID,donationID,title,description,category, location, imageURL;
    private Uri imageUri;
    private StorageTask uploadTask;
    private StorageReference storageDonationPicture;

    private String myUrl = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        getIntent().getParcelableExtra("donation");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        binding = ActivityUpdateDonationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();


        donorID = intent.getStringExtra(CONSTANTS.DONOR_ID);
        donationID = intent.getStringExtra(CONSTANTS.DONATION_ID);
        title = intent.getStringExtra(CONSTANTS.DONATION_TITLE);
        description = intent.getStringExtra(CONSTANTS.DONATION_DESCRIPTION);
        category = intent.getStringExtra(CONSTANTS.DONATION_CATEGORY);
        location = intent.getStringExtra(CONSTANTS.DONATION_LOCATION);
        imageURL = intent.getStringExtra(CONSTANTS.DONATION_IMAGE_URL);


        binding.nameEditText.setText(title);
        binding.descriptionEditText.setText(description);
        binding.categoryEditText.setText(category);
        binding.locationEditText.setText(location);
        Glide.with(this).load(imageURL).into(binding.heroImage);
        db= FirebaseFirestore.getInstance();
        storageDonationPicture = FirebaseStorage.getInstance().getReference().child(CONSTANTS.DONATIONS_PICTURES_PATH);


        binding.addNewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(UpdateDonationActivity.this);
            }
        });

        binding.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              saveDonation();
            }
        });
        binding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            binding.heroImage.setImageURI(imageUri);



        } else {
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UpdateDonationActivity.this, UpdateDonationActivity.class));
            finish();
        }
    }


    void uploadImage()
    {
        LoadingBar.showLoadingBar(UpdateDonationActivity.this, "Updating", "Please while we update information");
        if(imageUri!=null) {
            final StorageReference fileRef;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                fileRef = storageDonationPicture
                        .child(donorID + LocalDateTime.now().toString() + ".jpg");
                uploadTask = fileRef.putFile(imageUri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        if(taskSnapshot.getMetadata()!=null){
                            Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    myUrl = uri.toString();
                                    Donation donation = new Donation(
                                            donationID,
                                            binding.nameEditText.getText().toString().trim(),
                                            binding.descriptionEditText.getText().toString().trim(),
                                            binding.categoryEditText.getText().toString().trim(),
                                            myUrl,
                                            binding.locationEditText.getText().toString().trim(),
                                            donorID
                                    );
                                    db.collection(CONSTANTS.DONATION_COLLECTION_PATH)
                                            .document(donationID)
                                            .set(donation).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    LoadingBar.hideLoadingBar();
                                                    Toast.makeText(UpdateDonationActivity.this, "Donation Updated Successfully!", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(UpdateDonationActivity.this, DonorHomeActivity.class));
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    LoadingBar.hideLoadingBar();
                                                    Toast.makeText(UpdateDonationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                }
                            });
                        }

                    }
                });


            }

        }
        else {
            Donation donation = new Donation(
                    donationID,
                    binding.nameEditText.getText().toString().trim(),
                    binding.descriptionEditText.getText().toString().trim(),
                    binding.categoryEditText.getText().toString().trim(),
                    imageURL,
                    binding.locationEditText.getText().toString().trim(),
                    donorID
            );
            db.collection(CONSTANTS.DONATION_COLLECTION_PATH)
                    .document(donationID)
                    .set(donation).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            LoadingBar.hideLoadingBar();
                            Toast.makeText(UpdateDonationActivity.this, "Donation Updated Successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(UpdateDonationActivity.this, DonorHomeActivity.class));

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            LoadingBar.hideLoadingBar();
                            Toast.makeText(UpdateDonationActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();


                        }
                    });

        }



    }

    void saveDonation() {
        if (TextUtils.isEmpty(binding.nameEditText.getText())) {
            binding.nameLayout.setError(getString(R.string.title_cannot_be_null));
        }
        if (TextUtils.isEmpty(binding.descriptionEditText.getText())) {
            binding.descriptionLayout.setError(getString(R.string.description_cannot_be_null));

        }
        if (TextUtils.isEmpty(binding.categoryEditText.getText())) {
            binding.categoryLayout.setError(getString(R.string.category_cannot_be_null));

        }
        if (TextUtils.isEmpty(binding.locationEditText.getText())) {
            binding.locationLayout.setError(getString(R.string.location_cannot_be_null));

        }
        if (Objects.isNull(imageUri) && imageURL.isEmpty()) {
            binding.addNewImage.setVisibility(View.VISIBLE);
            binding.addNewImage.setText(getText(R.string.select_image));
            binding.addNewImage.setTextColor(getResources().getColor(R.color.my_app_error_container));

        } else {

            binding.nameLayout.setError(null);
            binding.descriptionLayout.setError(null);
            binding.categoryLayout.setError(null);
            binding.locationLayout.setError(null);
            binding.nameLayout.setError(null);


            uploadImage();

        }

    }


}
