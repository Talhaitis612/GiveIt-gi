package com.example.giveit_gi.DonorActivities;

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
import android.widget.Toast;

import com.example.giveit_gi.R;
import com.example.giveit_gi.Utils.CONSTANTS;
import com.example.giveit_gi.Utils.LoadingBar;
import com.example.giveit_gi.databinding.ActivityDonateThingsBinding;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public class DonateThingsActivity extends AppCompatActivity implements View.OnClickListener {
    private ActivityDonateThingsBinding binding;
    private Uri imageUri;

    private StorageReference storageDonationPicture;
    private FirebaseAuth mAuth;
    private StorageTask uploadTask;
    private String myUrl = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDonateThingsBinding.inflate(getLayoutInflater());
        Objects.requireNonNull(getSupportActionBar()).setTitle(R.string.donate_a_thing);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        setContentView(binding.getRoot());

        binding.addImage.setOnClickListener(this);
        binding.removeImage.setOnClickListener(this);
        binding.addDonationBtn.setOnClickListener(this);
        mAuth = FirebaseAuth.getInstance();
        storageDonationPicture = FirebaseStorage.getInstance().getReference().child(CONSTANTS.DONATIONS_PICTURES_PATH);



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
        if(id == R.id.add_image){
            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .start(DonateThingsActivity.this);

        }
        else if (id== R.id.remove_image){
            binding.addImage.setVisibility(View.VISIBLE);
            binding.heroImage.setImageDrawable(getDrawable(R.drawable.place_holder_image));
            binding.removeImage.setVisibility(View.INVISIBLE);
        }
        else if(id== R.id.addDonationBtn){
            saveDonation();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data!=null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            binding.addImage.setVisibility(View.INVISIBLE);
            binding.heroImage.setImageURI(imageUri);
            binding.removeImage.setVisibility(View.VISIBLE);


        }
        else {
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(DonateThingsActivity.this, DonateThingsActivity.class));
            finish();
        }
    }

//    To upload Image on
    void uploadImage(){
     if(imageUri!=null){

         if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
             final StorageReference fileRef = storageDonationPicture
                     .child(Objects.requireNonNull(mAuth.getCurrentUser()).getUid()+ LocalDateTime.now().toString()+".jpg");

             uploadTask = fileRef.putFile(imageUri);
             uploadTask.continueWith(new Continuation() {
                 @Override
                 public Object then(@NonNull Task task) throws Exception {
                     if(!task.isSuccessful()){
                         throw task.getException();
                     }
                     return fileRef.getDownloadUrl();
                 }
             }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                 @Override
                 public void onComplete(@NonNull Task<Uri> task) {
                     Uri downloadUrl = task.getResult();
                     myUrl = downloadUrl.toString();
                 }
             });
         }
     }
    }

    void saveDonation(){
        if(TextUtils.isEmpty(binding.nameEditText.getText())){
            binding.nameLayout.setError(getString(R.string.title_cannot_be_null));
        }
        if(TextUtils.isEmpty(binding.descriptionEditText.getText())){
            binding.descriptionLayout.setError(getString(R.string.description_cannot_be_null));

        }
        if(TextUtils.isEmpty(binding.categoryEditText.getText())){
            binding.categoryLayout.setError(getString(R.string.category_cannot_be_null));

        }
        if(TextUtils.isEmpty(binding.locationEditText.getText())){
            binding.locationLayout.setError(getString(R.string.location_cannot_be_null));

        }
        if(Objects.equals(myUrl, "")){
            binding.removeImage.setVisibility(View.VISIBLE);
            binding.removeImage.setText(getText(R.string.select_image));
            binding.removeImage.setTextColor(getResources().getColor(R.color.my_app_error_container));

        }

        else {
            binding.removeImage.setVisibility(View.VISIBLE);
            binding.removeImage.setText(getString(R.string.remove_image));
            binding.nameLayout.setError(null);
            binding.descriptionLayout.setError(null);
            binding.categoryLayout.setError(null);
            binding.locationLayout.setError(null);
            binding.nameLayout.setError(null);


        }

    }


}