package com.example.giveit_gi.ReceiverActivities.Adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.giveit_gi.DonorActivities.Interfaces.ItemClickListener;
import com.example.giveit_gi.Models.Donation;
import com.example.giveit_gi.R;
import com.example.giveit_gi.ReceiverActivities.CRUD.ItemFormActivity;
import com.example.giveit_gi.Utils.CONSTANTS;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.DonationViewHolder>  {

    Context context;
    ArrayList<Donation> donationArrayList;
    FirebaseFirestore db;
    ItemClickListener itemClickListener;



    public ItemsAdapter(Context context, ArrayList<Donation> donationArrayList, ItemClickListener itemClickListener) {
        this.context = context;
        this.donationArrayList = donationArrayList;
        this.itemClickListener = itemClickListener;
    }

    @NonNull
    @Override
    public ItemsAdapter.DonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.thing_item_layout, parent, false);

        return new ItemsAdapter.DonationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsAdapter.DonationViewHolder holder, @SuppressLint("RecyclerView") int position) {

        Donation donation = donationArrayList.get(position);


        if(donation.getisDonated()){
            holder.itemView.setVisibility(View.GONE);
        }

        Glide.with(context).load(donation.getImageURL()).into(holder.donationImageView);
        holder.txtDonationTitle.setText(donation.getTitle());
        holder.txtDonationDescription.setText(donation.getDescription());
        holder.txtDonationCategory.setText(donation.getCategory());
        holder.txtLocation.setText(donation.getLocation());
        SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a");

        holder.txtCreatedAt.setText(format.format(donation.getCreatedAt()));

        holder.getThingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ItemFormActivity.class);
                intent.putExtra(CONSTANTS.DONATION_ID, donation.getDonationID());
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
        return donationArrayList.size();
    }

    public static class DonationViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDonationTitle,txtDonationDescription,txtDonationCategory, txtLocation, txtCreatedAt;
        public ImageView donationImageView;
        Button getThingButton;
        private ItemClickListener clickListener;

        public DonationViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDonationTitle = itemView.findViewById(R.id.donation_title);
            txtDonationDescription = itemView.findViewById(R.id.donation_description);
            txtDonationCategory = itemView.findViewById(R.id.donation_category);
            txtLocation = itemView.findViewById(R.id.donation_location);
            txtCreatedAt = itemView.findViewById(R.id.donation_time);
            donationImageView = itemView.findViewById(R.id.donation_image);
            getThingButton = itemView.findViewById(R.id.getitemButton);



        }


    }


}
