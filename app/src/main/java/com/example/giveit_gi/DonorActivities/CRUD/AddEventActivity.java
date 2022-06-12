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

import com.example.giveit_gi.DonorActivities.CategoriesActivities.DonateThingsActivity;
import com.example.giveit_gi.DonorActivities.DonorHomeActivity;
import com.example.giveit_gi.Models.Donation;
import com.example.giveit_gi.R;
import com.example.giveit_gi.Shared.CategoriesActivities.EventActivity;
import com.example.giveit_gi.Shared.Models.Event;
import com.example.giveit_gi.Utils.CONSTANTS;
import com.example.giveit_gi.Utils.LoadingBar;
import com.example.giveit_gi.databinding.ActivityAddEventBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
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
import java.util.Calendar;
import java.util.Objects;

public class AddEventActivity extends AppCompatActivity  {

    ActivityAddEventBinding binding;
    int hour, min;
    String formattedTime;
    String am_pm = "";


    private Uri imageUri;

    private FirebaseAuth mAuth;
    private StorageTask uploadTask;
    private StorageReference storageEventPicture;

    private String myUrl = "";

    private String currentUserID = "";

    private FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getSupportActionBar().setTitle("Add Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.dateEditText.setOnClickListener(view -> showDatePickerDialog());
        binding.timeEditText.setOnClickListener(view -> showDateTimeDialog());

        binding.addImage.setOnClickListener(view -> {
            CropImage.activity(imageUri)
                    .start(AddEventActivity.this);
        });

        binding.removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.addImage.setVisibility(View.VISIBLE);
                binding.heroImage.setImageDrawable(getDrawable(R.drawable.place_holder_image));
                binding.removeImage.setVisibility(View.INVISIBLE);
            }
        });

        binding.addEvent.setOnClickListener(view -> addEvent());

        mAuth = FirebaseAuth.getInstance();
        currentUserID = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        storageEventPicture = FirebaseStorage.getInstance().getReference().child(CONSTANTS.EVENTS_PICTURES_PATH);
        db = FirebaseFirestore.getInstance();


    }



    private void showDateTimeDialog() {
        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Even Time")
                .build();
        materialTimePicker.show(getSupportFragmentManager(), "tag");
        materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hour = materialTimePicker.getHour();
                min = materialTimePicker.getMinute();
                if(hour>12){
                    am_pm = "PM";
                    if(min>10){
                        formattedTime = hour +":" + min +" "+ am_pm;
                    }
                    else {
                        formattedTime = hour +":0" + min + " "+ am_pm;
                    }
                }
                else {
                    am_pm = "AM";
                    if(min>10){
                        formattedTime = "0"+hour +":" + min + " "+ am_pm;
                    }
                    else {
                        formattedTime = "0"+hour +":0" + min + " "+ am_pm;
                    }
                }

                binding.timeEditText.setText(formattedTime);
            }
        });

    }

    private void showDatePickerDialog() {
        MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build();
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                binding.dateEditText.setText(materialDatePicker.getHeaderText());
            }
        });
        materialDatePicker.show(getSupportFragmentManager(), "LOG");

    }


    private void addEvent() {

        if (TextUtils.isEmpty(binding.titleEditText.getText())) {
            binding.nameLayout.setError(getString(R.string.title_cannot_be_null));
        }
        if (TextUtils.isEmpty(binding.descriptionEditText.getText())) {
            binding.descriptionLayout.setError(getString(R.string.description_cannot_be_null));

        }
        if (TextUtils.isEmpty(binding.dateEditText.getText())) {
            binding.dateLayout.setError(getString(R.string.choose_date));

        }
        if (TextUtils.isEmpty(binding.timeEditText.getText())) {
            binding.dateLayout.setError(getString(R.string.choose_time));

        }
        if (TextUtils.isEmpty(binding.locationEditText.getText())) {
            binding.locationLayout.setError(getString(R.string.location_cannot_be_null));

        }

        else {
            binding.removeImage.setVisibility(View.VISIBLE);
            binding.removeImage.setText(getString(R.string.remove_image));
            binding.nameLayout.setError(null);
            binding.descriptionLayout.setError(null);
            binding.dateLayout.setError(null);
            binding.locationLayout.setError(null);
            binding.timeLayout.setError(null);
            binding.nameLayout.setError(null);
            binding.removeImage.setText(getText(R.string.remove_image));
            binding.removeImage.setTextColor(getResources().getColor(R.color.black));

            uploadImage();

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
            startActivity(new Intent(AddEventActivity.this, DonateThingsActivity.class));
            finish();
        }
    }


    void uploadImage() {
        if (imageUri != null) {
            LoadingBar.showLoadingBar(this, getString(R.string.adding), getString(R.string.wait_event));
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                final StorageReference fileRef = storageEventPicture
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

                                        Event event = new Event(
                                                generateUniqueKey,
                                                binding.titleEditText.getText().toString().trim(),
                                                binding.descriptionEditText.getText().toString().trim(),
                                                formattedTime,
                                                myUrl,
                                                binding.locationEditText.getText().toString().trim(),
                                                currentUserID,
                                                false
                                        );

                                        db.collection(CONSTANTS.EVENT_COLLECTION_PATH)
                                                .document(generateUniqueKey).set(event)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isSuccessful())
                                                        {
                                                            Toast.makeText(AddEventActivity.this, "Event added successfully!", Toast.LENGTH_SHORT).show();

                                                            ArrayList<String> donationList = CONSTANTS.currentloggedInDonor.getDonationList();
                                                            donationList.add(generateUniqueKey);
                                                            CONSTANTS.currentloggedInDonor.setDonationList(donationList);
                                                            DocumentReference donorRef = db.collection(CONSTANTS.DONOR_COLLECTION_PATH).document(CONSTANTS.currentloggedInDonor.getUid());
                                                            donorRef.update("eventList", FieldValue.arrayUnion(generateUniqueKey));
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

        else {
            String generateUniqueKey = CONSTANTS.generateUniqueKey(25);

            Event event = new Event(
                    generateUniqueKey,
                    binding.titleEditText.getText().toString().trim(),
                    binding.descriptionEditText.getText().toString().trim(),
                    formattedTime,
                    myUrl,
                    binding.locationEditText.getText().toString().trim(),
                    currentUserID,
                    false
            );

            db.collection(CONSTANTS.EVENT_COLLECTION_PATH)
                    .document(generateUniqueKey).set(event)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(AddEventActivity.this, "Event added successfully!", Toast.LENGTH_SHORT).show();

                                ArrayList<String> donationList = CONSTANTS.currentloggedInDonor.getDonationList();
                                donationList.add(generateUniqueKey);
                                CONSTANTS.currentloggedInDonor.setDonationList(donationList);
                                DocumentReference donorRef = db.collection(CONSTANTS.DONOR_COLLECTION_PATH).document(CONSTANTS.currentloggedInDonor.getUid());
                                donorRef.update("eventList", FieldValue.arrayUnion(generateUniqueKey));
                                finish();


                            }
                        }
                    });

        }
    }


    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}