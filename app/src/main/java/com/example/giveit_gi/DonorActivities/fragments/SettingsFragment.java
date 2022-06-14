package com.example.giveit_gi.DonorActivities.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.giveit_gi.DonorActivities.CRUD.UpdateDonationActivity;
import com.example.giveit_gi.Models.Donation;
import com.example.giveit_gi.Models.Donor;
import com.example.giveit_gi.R;
import com.example.giveit_gi.Utils.CONSTANTS;
import com.example.giveit_gi.Utils.LoadingBar;
import com.example.giveit_gi.databinding.FragmentHomeBinding;
import com.example.giveit_gi.databinding.FragmentSettingsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Objects;


public class SettingsFragment extends Fragment {

    private FragmentSettingsBinding binding;
    private Uri imageUri;
    private FirebaseFirestore db;
    private StorageTask uploadTask;
    private StorageReference storageDonationPicture;

    private String myUrl = "";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();

        loadUserData();

        binding.profileChangeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CropImage.activity(imageUri)
                        .start(getActivity());
            }
        });

        binding.updateInformationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateInformation();
            }
        });

        db= FirebaseFirestore.getInstance();
        storageDonationPicture = FirebaseStorage.getInstance().getReference().child(CONSTANTS.DONOR_PICTURES_PATH);

        return binding.getRoot();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            binding.profileImageView.setImageURI(imageUri);



        } else {
            Toast.makeText(getActivity(), "Error, Try Again.", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }
    }


    private void updateInformation(){
          Editable donorName = binding.nameEditText.getText();
          Editable donorEmail = binding.emailEditText.getText();
          Editable phoneNumber = binding.phoneEditText.getText();

          if(TextUtils.isEmpty(donorName)){
              binding.nameLayout.setError(getString(R.string.err_name_empty));
          }

        if(TextUtils.isEmpty(donorEmail)){
            binding.emailLayout.setError(getString(R.string.err_name_empty));
        }

        if(TextUtils.isEmpty(phoneNumber)){
            binding.phoneLayout.setError(getString(R.string.err_name_empty));
        }

        else {

            uploadImage();
        }






    }

    private void loadUserData(){

        binding.nameEditText.setText(CONSTANTS.currentloggedInDonor.getName());
        binding.emailEditText.setText(CONSTANTS.currentloggedInDonor.getEmail());
        binding.phoneEditText.setText(CONSTANTS.currentloggedInDonor.getPhoneNumber());


        if(Objects.equals(CONSTANTS.currentloggedInDonor.getProfilePicture(), "")){

        }
        else {
            Glide.with(getActivity()).load(CONSTANTS.currentloggedInDonor.getProfilePicture()).into(binding.profileImageView);
        }

//        db.collection(CONSTANTS.DONOR_COLLECTION_PATH).
//                document(CONSTANTS.currentloggedInDonor.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
//
//                    }
//                });

    }


    private void uploadImage(){
        LoadingBar.showLoadingBar(getActivity(), "Updating", "Please while we update your information");

        if(imageUri!=null) {
            final StorageReference fileRef;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                fileRef = storageDonationPicture
                        .child(CONSTANTS.currentloggedInDonor.getUid() + LocalDateTime.now().toString() + ".jpg");
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

                                    db.collection(CONSTANTS.DONATION_COLLECTION_PATH)
                                            .document(CONSTANTS.currentloggedInDonor.getUid())
                                            .update("")
                                }
                            });
                        }

                    }
                });


            }

        }


    }


}