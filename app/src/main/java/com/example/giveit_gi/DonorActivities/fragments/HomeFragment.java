package com.example.giveit_gi.DonorActivities.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.giveit_gi.DonorActivities.CRUD.AddEventActivity;
import com.example.giveit_gi.DonorActivities.CategoriesActivities.DonateMoneyActivity;
import com.example.giveit_gi.DonorActivities.CategoriesActivities.DonateThingsActivity;
import com.example.giveit_gi.R;
import com.example.giveit_gi.databinding.FragmentHomeBinding;


public class HomeFragment extends Fragment implements View.OnClickListener {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        binding.donateAThing.setOnClickListener(this);
        binding.donateMoney.setOnClickListener(this);
        binding.addEvent.setOnClickListener(this);
        binding.suggestionBox.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.donate_a_thing) {
            startActivity(new Intent(getActivity(), DonateThingsActivity.class));
        }
        if(id== R.id.donate_money){
            startActivity(new Intent(getActivity(), DonateMoneyActivity.class));

        }
        if(id==R.id.add_event){
            startActivity(new Intent(getActivity(), AddEventActivity.class));

        }
    }
}