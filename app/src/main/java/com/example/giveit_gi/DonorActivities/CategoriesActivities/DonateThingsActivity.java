package com.example.giveit_gi.DonorActivities.CategoriesActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.giveit_gi.DonorActivities.DonorHomeActivity;
import com.example.giveit_gi.Models.Donation;
import com.example.giveit_gi.R;
import com.example.giveit_gi.Utils.CONSTANTS;
import com.example.giveit_gi.Utils.LoadingBar;
import com.example.giveit_gi.databinding.ActivityDonateThingsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
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

public class DonateThingsActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityDonateThingsBinding binding;
    private Uri imageUri;

    private FirebaseAuth mAuth;
    private StorageTask uploadTask;
    private StorageReference storageDonationPicture;

    private String myUrl = "";

    private String currentUserID = "";

    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        binding = ActivityDonateThingsBinding.inflate(getLayoutInflater());
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.donate_a_thing);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(binding.getRoot());

        binding.addImage.setOnClickListener(this);
        binding.removeImage.setOnClickListener(this);
        binding.addDonationBtn.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        storageDonationPicture = FirebaseStorage.getInstance().getReference().child(CONSTANTS.DONATIONS_PICTURES_PATH);
        db = FirebaseFirestore.getInstance();


    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    @Override
    public void onClick(View view) {

        int id = view.getId();
        if (id == R.id.add_image) {
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(DonateThingsActivity.this);

        } else if (id == R.id.remove_image) {
            binding.addImage.setVisibility(View.VISIBLE);
            binding.heroImage.setImageDrawable(getDrawable(R.drawable.place_holder_image));
            binding.removeImage.setVisibility(View.INVISIBLE);
        } else if (id == R.id.addDonationBtn) {
            saveDonation();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            binding.addImage.setVisibility(View.INVISIBLE);
            binding.heroImage.setImageURI(imageUri);
            binding.removeImage.setVisibility(View.VISIBLE);


        } else {
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(DonateThingsActivity.this, DonateThingsActivity.class));
            finish();
        }
    }

    //    To upload Image on
    void uploadImage() {
        if (imageUri != null) {
            LoadingBar.showLoadingBar(this, getString(R.string.adding), getString(R.string.wait));
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                final StorageReference fileRef = storageDonationPicture
                        .child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid() + LocalDateTime.now().toString() + ".jpg");

                uploadTask = fileRef.putFile(imageUri);
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        if (taskSnapshot.getMetadata() != null) {
                            if (taskSnapshot.getMetadata().getReference() != null) {
                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        myUrl = uri.toString();
                                        String generateUniqueKey = CONSTANTS.generateUniqueKey(25);

                                        Donation donation = new Donation(
                                                generateUniqueKey,
                                                binding.nameEditText.getText().toString().trim(),
                                                binding.descriptionEditText.getText().toString().trim(),
                                                binding.categoryEditText.getText().toString().trim(),
                                                myUrl,
                                                binding.locationEditText.getText().toString().trim(),
                                                currentUserID
                                        );

                                        db.collection(CONSTANTS.DONATION_COLLECTION_PATH)
                                                .document(generateUniqueKey).set(donation)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            Toast.makeText(DonateThingsActivity.this, "Donation added successfully!", Toast.LENGTH_SHORT).show();

                                                            ArrayList<String> donationList = CONSTANTS.currentloggedInDonor.getDonationList();
                                                            donationList.add(generateUniqueKey);
                                                            CONSTANTS.currentloggedInDonor.setDonationList(donationList);
                                                            DocumentReference donorRef = db.collection(CONSTANTS.DONOR_COLLECTION_PATH).document(CONSTANTS.currentloggedInDonor.getUid());
                                                            donorRef.update("donationList", FieldValue.arrayUnion(generateUniqueKey));


                                                            Snackbar.make(binding.getRoot(), "Donation added successfully!", Snackbar.LENGTH_LONG)
                                                                    .setAction("Action", null).show();
                                                            startActivity(new Intent(DonateThingsActivity.this, DonorHomeActivity.class));
                                                            finish();


                                                        }
                                                    }
                                                });

                                    }
                                });
                            }
                        }
                    }
                });
            }

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
        if (Objects.isNull(imageUri)) {
            binding.removeImage.setVisibility(View.VISIBLE);
            binding.removeImage.setText(getText(R.string.select_image));
            binding.removeImage.setTextColor(getResources().getColor(R.color.my_app_error_container));

        } else {
            binding.removeImage.setVisibility(View.VISIBLE);
            binding.removeImage.setText(getString(R.string.remove_image));
            binding.nameLayout.setError(null);
            binding.descriptionLayout.setError(null);
            binding.categoryLayout.setError(null);
            binding.locationLayout.setError(null);
            binding.nameLayout.setError(null);
            binding.removeImage.setText(getText(R.string.remove_image));
            binding.removeImage.setTextColor(getResources().getColor(R.color.black));

            uploadImage();

        }

    }


}