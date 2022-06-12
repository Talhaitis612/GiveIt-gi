package com.example.giveit_gi.Shared.CategoriesActivities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.giveit_gi.DonorActivities.Adapters.MoneyDonationAdapter;
import com.example.giveit_gi.DonorActivities.CRUD.AddEventActivity;
import com.example.giveit_gi.DonorActivities.Interfaces.ItemClickListener;
import com.example.giveit_gi.Models.ApplyDonation;
import com.example.giveit_gi.Models.Donation;
import com.example.giveit_gi.Shared.Adapters.EventAdapter;
import com.example.giveit_gi.Shared.Models.Event;
import com.example.giveit_gi.Utils.CONSTANTS;
import com.example.giveit_gi.databinding.ActivityEventBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Objects;

public class EventActivity extends AppCompatActivity implements ItemClickListener {

    private ActivityEventBinding binding;
    private FirebaseFirestore db;
    private ArrayList<Event> eventArrayList;
    private EventAdapter eventAdapter;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        getSupportActionBar().setTitle("Events");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching event List...");
        progressDialog.show();

        try {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
            if (!TextUtils.isEmpty(uid)){
                binding.addEventFab.setVisibility(View.VISIBLE);
                binding.addEventFab.setOnClickListener(view -> startActivity(new Intent(EventActivity.this, AddEventActivity.class)));

            }

        }
        catch (Exception e){
            Log.e("Error", e.getMessage());

        }





        binding.eventsRecycler.setHasFixedSize(true);
        binding.eventsRecycler.setLayoutManager(new LinearLayoutManager(this));
        db = FirebaseFirestore.getInstance();









        eventArrayList = new ArrayList<>();
        eventAdapter = new EventAdapter(this, eventArrayList,this);
        loadEventList();

        binding.eventsRecycler.setAdapter(eventAdapter);









    }

    private void loadEventList() {

        db.collection(CONSTANTS.EVENT_COLLECTION_PATH)
                .whereEqualTo("isEventCompleted", false)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            Toast.makeText(EventActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            Log.e("ERROR", error.getMessage());
                            if(progressDialog.isShowing()){
                                progressDialog.dismiss();
                            }
                            return;
                        }
                        assert value != null;
                        eventArrayList.clear();
                        if(value.isEmpty()){
                            progressDialog.dismiss();
                            return;
                        }
                        for (QueryDocumentSnapshot doc: value){
                            Event event = new Event(
                                    doc.getString("eventID"),
                                    doc.getString("eventTitle"),
                                    doc.getString("eventDescription"),
                                    doc.getString("eventDateTime"),
                                    doc.getString("eventImageURL"),
                                    doc.getString("eventLocation"),
                                    doc.getString("donorID"),
                                    doc.getBoolean("isEventCompleted")
                            );
                            eventArrayList.add(event);
                            eventAdapter.notifyDataSetChanged();
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

    @Override
    public void onClick(View view, int position) {
    }
}