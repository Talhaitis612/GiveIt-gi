package com.example.giveit_gi.ReceiverActivities.CategoriesActivities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.giveit_gi.DonorActivities.Interfaces.ItemClickListener;
import com.example.giveit_gi.Shared.Adapters.EventAdapter;
import com.example.giveit_gi.Shared.Models.Event;
import com.example.giveit_gi.Utils.CONSTANTS;
import com.example.giveit_gi.databinding.ActivityEventBinding;
import com.example.giveit_gi.databinding.ActivityEventReceiverBinding;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class EventActivityReceiver extends AppCompatActivity implements ItemClickListener {

    private ActivityEventReceiverBinding binding;
    private FirebaseFirestore db;
    private ArrayList<Event> eventArrayList;
    private EventAdapter eventAdapter;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEventReceiverBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        getSupportActionBar().setTitle("Events");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Fetching event List...");
        progressDialog.show();




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
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error!=null){
                            Toast.makeText(EventActivityReceiver.this, error.getMessage(), Toast.LENGTH_LONG).show();
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