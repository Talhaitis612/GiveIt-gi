package com.example.giveit_gi.DonorActivities.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.giveit_gi.DonorActivities.Adapters.DonationAdapter;
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

import java.util.ArrayList;


public class DonationListFragment extends Fragment {

    private FragmentDonationListBinding binding;
    private FirebaseFirestore db;
    ArrayList<Donation> donationArrayList;
    DonationAdapter donationAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        
        binding = FragmentDonationListBinding.inflate(inflater, container, false);
        binding.donationRecycler.setHasFixedSize(true);
        binding.donationRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        db = FirebaseFirestore.getInstance();
        donationArrayList = new ArrayList<Donation>();
        donationAdapter = new DonationAdapter(getContext(), donationArrayList);
        loadDonationList();


        return binding.getRoot();
    }

    private void loadDonationList() {
        db.collection(CONSTANTS.DONATION_COLLECTION_PATH)
                .orderBy("title", Query.Direction.ASCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null){
                            Log.e("Firebase Error: ", error.getMessage());
                            return;
                        }

                        for (DocumentChange dc: value.getDocumentChanges()){
                            if(dc.getType() == DocumentChange.Type.ADDED){
                                donationArrayList.add(dc.getDocument().toObject(Donation.class));

                            }
                            donationAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
       }
}