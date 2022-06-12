package com.example.giveit_gi.DonorActivities.CRUD;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.giveit_gi.databinding.ActivityAddEventBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

public class AddEventActivity extends AppCompatActivity {

    ActivityAddEventBinding binding;
    int hour, min;
    String formattedTime;
    String am_pm = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getSupportActionBar().setTitle("Add Event");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        binding.dateEditText.setOnClickListener(view -> showDatePickerDialog());
        binding.timeEditText.setOnClickListener(view -> showDateTimeDialog());

    }

    private void showDateTimeDialog() {
        MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_24H)
                .setHour(12)
                .setMinute(0)
                .setTitleText("Select Even Time")
                .build();
        materialTimePicker.show(getSupportFragmentManager(), "tag");
        materialTimePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hour = materialTimePicker.getHour();
                min = materialTimePicker.getMinute();
                if(hour>12){
                    am_pm = "PM";
                    if(min>10){
                        formattedTime = hour +":" + min +" "+ am_pm;
                    }
                    else {
                        formattedTime = hour +":0" + min + " "+ am_pm;
                    }
                }
                else {
                    am_pm = "AM";
                    if(min>10){
                        formattedTime = "0"+hour +":" + min + " "+ am_pm;
                    }
                    else {
                        formattedTime = "0"+hour +":0" + min + " "+ am_pm;
                    }
                }

                binding.timeEditText.setText(formattedTime);
            }
        });

    }

    private void showDatePickerDialog() {
        MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select date")
                .build();
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                binding.dateEditText.setText(materialDatePicker.getHeaderText());
            }
        });
        materialDatePicker.show(getSupportFragmentManager(), "LOG");

    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
}