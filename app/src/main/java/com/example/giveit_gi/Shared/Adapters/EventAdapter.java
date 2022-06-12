package com.example.giveit_gi.Shared.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.giveit_gi.DonorActivities.CRUD.UpdateDonationActivity;
import com.example.giveit_gi.DonorActivities.Interfaces.ItemClickListener;
import com.example.giveit_gi.Models.Donation;
import com.example.giveit_gi.R;
import com.example.giveit_gi.Shared.Models.Event;
import com.example.giveit_gi.Utils.CONSTANTS;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.DonationViewHolder>  {

    Context context;
    ArrayList<Event> eventArrayList;
    FirebaseFirestore db;
    ItemClickListener itemClickListener;



    public EventAdapter(Context context, ArrayList<Event> eventArrayList, ItemClickListener itemClickListener) {
        this.context = context;
        this.eventArrayList = eventArrayList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public EventAdapter.DonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.event_item_layout, parent, false);

        return new DonationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.DonationViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Event event = eventArrayList.get(position);
        if(Objects.equals(event.getDonorID(), CONSTANTS.currentloggedInDonor.getUid())){
            holder.buttonLayout.setVisibility(View.VISIBLE);
        }



        Glide.with(context).load(event.getEventImageURL()).into(holder.eventImageView);
        holder.eventTitle.setText(event.getEventTitle());
        holder.eventDescription.setText(event.getEventDescription());
        holder.eventDateTime.setText(event.getEventDateTime());
        holder.eventLocation.setText(event.getEventLocation());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage(context.getResources().getString(R.string.do_you_really_want_to_event));
                builder.setTitle(context.getResources().getString(R.string.delete));
                builder.setCancelable(false);
                builder.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setPositiveButton(context.getResources().getString(R.string.sure), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        db = FirebaseFirestore.getInstance();

                        db.collection(CONSTANTS.EVENT_COLLECTION_PATH)
                                .document(event.getEventID())
                                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        DocumentReference donorRef = db.collection(CONSTANTS.DONOR_COLLECTION_PATH).document(CONSTANTS.currentloggedInDonor.getUid());
                                        donorRef.update("eventList", FieldValue.arrayRemove(event.getEventID())).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(context, "Event Deleted Successfully!", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });



                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });
        holder.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateDonationActivity.class);
                intent.putExtra(CONSTANTS.DONOR_ID, event.getDonorID());
                intent.putExtra(CONSTANTS.DONATION_ID, event.getEventID());
                intent.putExtra(CONSTANTS.DONATION_TITLE, event.getEventTitle());
                intent.putExtra(CONSTANTS.DONATION_DESCRIPTION, event.getEventDescription());
                intent.putExtra(CONSTANTS.DONATION_LOCATION, event.getEventLocation());
                intent.putExtra(CONSTANTS.DONATION_IMAGE_URL, event.getEventImageURL());
                intent.putExtra(CONSTANTS.DONATION_TIME, event.getEventDateTime());


                context.startActivity(intent);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onClick(view,position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return eventArrayList.size();
    }

    public static class DonationViewHolder extends RecyclerView.ViewHolder {
        public TextView eventTitle,eventDescription,eventDateTime, eventLocation, txtDate;
        public ImageView eventImageView;
        public LinearLayout buttonLayout;
        Button deleteButton, updateButton;

        public DonationViewHolder(@NonNull View itemView) {
            super(itemView);
            eventTitle = itemView.findViewById(R.id.event_title);
            eventDescription = itemView.findViewById(R.id.event_description);
            eventDateTime = itemView.findViewById(R.id.eventdateTime);
            eventLocation = itemView.findViewById(R.id.event_location);
            txtDate = itemView.findViewById(R.id.donation_time);
            eventImageView = itemView.findViewById(R.id.event_image);
            buttonLayout = itemView.findViewById(R.id.buttonLayout);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            updateButton  = itemView.findViewById(R.id.updateButton);




        }


    }


}
