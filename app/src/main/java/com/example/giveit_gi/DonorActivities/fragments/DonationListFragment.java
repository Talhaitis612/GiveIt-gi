package com.example.giveit_gi.DonorActivities.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.giveit_gi.R;
import com.example.giveit_gi.databinding.FragmentDonationListBinding;


public class DonationListFragment extends Fragment {

    private FragmentDonationListBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDonationListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}