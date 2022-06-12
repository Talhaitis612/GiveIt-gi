package com.example.giveit_gi.DonorActivities.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.giveit_gi.R;
import com.example.giveit_gi.databinding.FragmentDonationListBinding;
import com.example.giveit_gi.databinding.FragmentHistoryBinding;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {

    FragmentHistoryBinding binding;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        loadPieChartData();
        setUpPieChart();
        return binding.getRoot();
    }


    private  void setUpPieChart(){
        binding.pieChart.setDrawHoleEnabled(true);
        binding.pieChart.setUsePercentValues(true);
        binding.pieChart.setEntryLabelTextSize(12);
        binding.pieChart.setEntryLabelColor(Color.BLACK);
        binding.pieChart.setCenterText("Overall donation chart");
        binding.pieChart.setCenterTextSize(25);
        binding.pieChart.getDescription().setEnabled(false);
        Legend legend = binding.pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setOrientation(Legend.LegendOrientation.VERTICAL);
        legend.setDrawInside(false);
        legend.setEnabled(true);

    }


    private void loadPieChartData() {

        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(0.2f, "Events Organization"));
        entries.add(new PieEntry(0.15f, "Donation of Items"));
        entries.add(new PieEntry(0.10f, "Contribution in other activities"));
        entries.add(new PieEntry(0.25f, "Food & Drinking"));
        entries.add(new PieEntry(0.23f, "Food & Drinking"));

        ArrayList<Integer> colors = new ArrayList<>();

        for (int color : ColorTemplate.MATERIAL_COLORS){
            colors.add(color);
        }
        for(int color: ColorTemplate.VORDIPLOM_COLORS){
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Expense Category");
        dataSet.setColors(colors);
        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(binding.pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);
        binding.pieChart.setData(data);
        binding.pieChart.invalidate();


    }
}