package com.example.giveit_gi.DonorActivities.CategoriesActivities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.giveit_gi.DonorActivities.Adapters.DonationAdapter;
import com.example.giveit_gi.DonorActivities.Adapters.MoneyDonationAdapter;
import com.example.giveit_gi.Models.ApplyDonation;
import com.example.giveit_gi.Models.Donation;
import com.example.giveit_gi.Utils.CONSTANTS;
import com.example.giveit_gi.databinding.ActivityDonateMoneyBinding;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class DonateMoneyActivity extends AppCompatActivity  {

    private ActivityDonateMoneyBinding binding;
    private FirebaseFirestore db;
    private ArrayList<ApplyDonation> donationArrayList;
    private MoneyDonationAdapter donationAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching donation List...");
        progressDialog.show();
        binding = ActivityDonateMoneyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Objects.requireNonNull(getSupportActionBar()).setTitle("Donate Money");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.donationMoneyRecycler.setHasFixedSize(true);
        binding.donationMoneyRecycler.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();


        donationArrayList = new ArrayList<>();
        donationAdapter = new MoneyDonationAdapter(this, donationArrayList);
        loadRequestedDonationList();

        binding.donationMoneyRecycler.setAdapter(donationAdapter);


    }

    private void loadRequestedDonationList() {
        db.collection(CONSTANTS.APPLY_ITEM_COLLECTION_PATH)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            Toast.makeText(DonateMoneyActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("ERROR", error.getMessage());
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            return;
                        }
                        donationArrayList.clear();
                        assert value != null;
                        for (QueryDocumentSnapshot doc: value){
                            ApplyDonation donation = new ApplyDonation(
                                    doc.getString("aplDonationID"),
                                    doc.getString("name"),
                                    doc.getString("email"),
                                    doc.getString("idCardNumber"),
                                    doc.getString("phoneNo"),
                                    doc.getString("title"),
                                    doc.getString("problem"),
                                    doc.getString("location"),
                                    doc.getString("amountNeeded"),
                                    doc.getLong("amountReceived"),
                                    doc.getBoolean("isApproved"),
                                    doc.getDate("createdAt")
                            );
                            donationArrayList.add(donation);
                            donationAdapter.notifyDataSetChanged();
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }


                        }

                    }
                });

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}