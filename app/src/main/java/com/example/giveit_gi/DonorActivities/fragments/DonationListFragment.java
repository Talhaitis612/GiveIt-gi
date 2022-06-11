package com.example.giveit_gi.DonorActivities.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.giveit_gi.DonorActivities.Adapters.DonationAdapter;
import com.example.giveit_gi.DonorActivities.CRUD.ViewDonationActivity;
import com.example.giveit_gi.DonorActivities.Interfaces.ItemClickListener;
import com.example.giveit_gi.Models.Donation;
import com.example.giveit_gi.Utils.CONSTANTS;
import com.example.giveit_gi.databinding.FragmentDonationListBinding;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class  DonationListFragment extends Fragment implements ItemClickListener {

    private FragmentDonationListBinding binding;
    private FirebaseFirestore db;
    ArrayList<Donation> donationArrayList;
    public DonationAdapter donationAdapter;
    ProgressDialog progressDialog;


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
        donationArrayList = new ArrayList<>();
        donationAdapter = new DonationAdapter(getContext(), donationArrayList,this);
        binding.donationRecycler.setAdapter(donationAdapter);
        loadDonationList();
        binding.swipeRefresh.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) () -> new Handler().postDelayed(new Runnable() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void run() {
                donationAdapter.notifyDataSetChanged();
                binding.swipeRefresh.setRefreshing(false);
            }
        }, 2000));



        return binding.getRoot();
    }

    private void loadDonationList() {
        db.collection(CONSTANTS.DONATION_COLLECTION_PATH)
                .orderBy("createdAt", Query.Direction.DESCENDING )
                .whereEqualTo("isDonated", false)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                     if(error!=null){
                         Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_LONG).show();
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