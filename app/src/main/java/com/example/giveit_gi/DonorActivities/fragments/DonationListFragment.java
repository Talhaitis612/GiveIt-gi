package com.example.giveit_gi.DonorActivities.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.giveit_gi.DonorActivities.Adapters.DonationAdapter;
import com.example.giveit_gi.DonorActivities.CRUD.ViewDonationActivity;
import com.example.giveit_gi.DonorActivities.Interfaces.ItemClickListener;
import com.example.giveit_gi.Models.Donation;
import com.example.giveit_gi.R;
import com.example.giveit_gi.Utils.CONSTANTS;
import com.example.giveit_gi.databinding.FragmentDonationListBinding;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;


public class  DonationListFragment extends Fragment implements ItemClickListener {

    private FragmentDonationListBinding binding;
    private FirebaseFirestore db;
    ArrayList<Donation> donationArrayList;
    public DonationAdapter donationAdapter;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching donation List...");
        progressDialog.show();


        binding = FragmentDonationListBinding.inflate(inflater, container, false);
        binding.donationRecycler.setHasFixedSize(true);
        binding.donationRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        db = FirebaseFirestore.getInstance();
        donationArrayList = new ArrayList<Donation>();
        donationAdapter = new DonationAdapter(getContext(), donationArrayList,this);
        binding.donationRecycler.setAdapter(donationAdapter);
        loadDonationList();
        binding.swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void run() {
                        donationAdapter.notifyDataSetChanged();
                        binding.swipeRefresh.setRefreshing(false);
                    }
                }, 2000);
            }
        });



        return binding.getRoot();
    }

    private void loadDonationList() {
        db.collection(CONSTANTS.DONATION_COLLECTION_PATH)
                .orderBy("createdAt", Query.Direction.DESCENDING )
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            Log.e("Firebase Error: ", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc: value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                donationArrayList.add(dc.getDocument().toObject(Donation.class));

                            }
                            if(dc.getType()== DocumentChange.Type.REMOVED){
                                donationAdapter.notifyDataSetChanged();

                            }
                            if(dc.getType()== DocumentChange.Type.MODIFIED){
                                donationAdapter.notifyDataSetChanged();

                            }
                            donationAdapter.notifyDataSetChanged();
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
       }


    @Override
    public void onClick(View view, int position) {
        final Donation donation = donationArrayList.get(position);
        Intent intent = new Intent(getActivity(), ViewDonationActivity.class);
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