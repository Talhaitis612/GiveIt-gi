package com.example.giveit_gi.ReceiverActivities.CategoriesActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.giveit_gi.DonorActivities.Adapters.DonationAdapter;
import com.example.giveit_gi.DonorActivities.CRUD.ViewDonationActivity;
import com.example.giveit_gi.DonorActivities.Interfaces.ItemClickListener;
import com.example.giveit_gi.Models.Donation;
import com.example.giveit_gi.R;
import com.example.giveit_gi.ReceiverActivities.Adapters.ItemsAdapter;
import com.example.giveit_gi.ReceiverActivities.CRUD.ViewSingleItemActivity;
import com.example.giveit_gi.Utils.CONSTANTS;
import com.example.giveit_gi.Utils.LoadingBar;
import com.example.giveit_gi.databinding.ActivityItemListBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Objects;

public class ItemListActivity extends AppCompatActivity implements ItemClickListener {
    ActivityItemListBinding binding;
    private FirebaseFirestore db;
    ArrayList<Donation> itemArrayList;
    public ItemsAdapter itemsAdapter;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching donation List...");
        progressDialog.show();
        binding = ActivityItemListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.get_a_thing));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.itemRecycler.setHasFixedSize(true);
        binding.itemRecycler.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();
        itemArrayList = new ArrayList<Donation>();
        itemsAdapter = new ItemsAdapter(this, itemArrayList,this);
        binding.itemRecycler.setAdapter(itemsAdapter);
        loadItemsList();
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        itemsAdapter.notifyDataSetChanged();
                        binding.swipeRefresh.setRefreshing(false);
                    }
                }, 2000);
            }
        });


    }

    private void loadItemsList() {
        db.collection(CONSTANTS.DONATION_COLLECTION_PATH)
                .orderBy("createdAt", Query.Direction.DESCENDING )
                .whereEqualTo("isDonated", false)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            Toast.makeText(ItemListActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("ERROR", error.getMessage());
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            return;
                        }
                        assert value != null;
                        for (QueryDocumentSnapshot doc: value){
                            Donation donation = new Donation(
                                    doc.getString("donationID"),
                                    doc.getString("title"),
                                    doc.getString("description"),
                                    doc.getString("category"),
                                    doc.getString("imageURL"),
                                    doc.getString("location"),
                                    doc.getString("donorID"),
                                    doc.getBoolean("isDonated"),
                                    doc.getDate("createdAt")
                            );
                            itemArrayList.add(donation);
                            itemsAdapter.notifyDataSetChanged();
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
        return true;
    }

    @Override
    public void onClick(View view, int position) {
        final Donation donation = itemArrayList.get(position);
        Intent intent = new Intent(ItemListActivity.this, ViewSingleItemActivity.class);
        intent.putExtra(CONSTANTS.DONATION_ID, donation.getDonationID());

        intent.putExtra(CONSTANTS.DONOR_ID, donation.getDonorID());
        intent.putExtra(CONSTANTS.DONATION_TITLE, donation.getTitle());
        intent.putExtra(CONSTANTS.DONATION_DESCRIPTION, donation.getDescription());
        intent.putExtra(CONSTANTS.DONATION_CATEGORY, donation.getCategory());
        intent.putExtra(CONSTANTS.DONATION_LOCATION, donation.getLocation());
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a");
        intent.putExtra(CONSTANTS.DONATION_TIME, format.format(donation.getCreatedAt()));
        intent.putExtra(CONSTANTS.DONATION_IMAGE_URL, donation.getImageURL());
        startActivity(intent);
    }
}