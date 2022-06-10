package com.example.giveit_gi.ReceiverActivities.CategoriesActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.giveit_gi.DonorActivities.Adapters.DonationAdapter;
import com.example.giveit_gi.DonorActivities.Interfaces.ItemClickListener;
import com.example.giveit_gi.Models.Donation;
import com.example.giveit_gi.R;
import com.example.giveit_gi.ReceiverActivities.Adapters.ItemsAdapter;
import com.example.giveit_gi.Utils.CONSTANTS;
import com.example.giveit_gi.databinding.ActivityItemListBinding;
import com.google.firebase.firestore.FirebaseFirestore;

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
        db.collection(CONSTANTS.DONATION_COLLECTION_PATH);


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    public void onClick(View view, int position) {

    }
}