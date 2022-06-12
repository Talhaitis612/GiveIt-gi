package com.example.giveit_gi.DonorActivities.CategoriesActivities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
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

public class DonateMoneyActivity extends AppCompatActivity  {

    private ActivityDonateMoneyBinding binding;
    private FirebaseFirestore db;
    private ArrayList<ApplyDonation> donationArrayList;
    private MoneyDonationAdapter donationAdapter;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDonateMoneyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().setTitle("Donate Money");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching donation List...");
        progressDialog.show();
        binding.donationMoneyRecycler.setHasFixedSize(true);
        binding.donationMoneyRecycler.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();
        donationArrayList = new ArrayList<>();
        donationAdapter = new MoneyDonationAdapter(this, donationArrayList);
        binding.donationMoneyRecycler.setAdapter(donationAdapter);
        loadRequestedDonationList();
    }

    private void loadRequestedDonationList() {
        db.collection(CONSTANTS.REQUEST_ITEM_COLLECTION_PATH)
                .orderBy("appliedTime", Query.Direction.DESCENDING )
                .whereEqualTo("isApproved", false)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                                    Integer.parseInt(doc.getString("amountReceived")),
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