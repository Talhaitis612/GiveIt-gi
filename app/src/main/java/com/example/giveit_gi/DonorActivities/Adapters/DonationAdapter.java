package com.example.giveit_gi.DonorActivities.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.giveit_gi.Models.Donation;
import com.example.giveit_gi.R;

import java.util.ArrayList;

public class DonationAdapter extends RecyclerView.Adapter<DonationAdapter.DonationViewHolder> {

    Context context;
    ArrayList<Donation> donationArrayList;

    public DonationAdapter(Context context, ArrayList<Donation> donationArrayList) {
        this.context = context;
        this.donationArrayList = donationArrayList;
    }

    @NonNull
    @Override
    public DonationAdapter.DonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.donation_item_layout, parent, false);

        return new DonationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonationAdapter.DonationViewHolder holder, int position) {

        Donation donation = donationArrayList.get(position);
        Glide.with(context).load(donation.getImageURL()).into(holder.donationImageView);
        holder.txtDonationTitle.setText(donation.getTitle());
        holder.txtDonationDescription.setText(donation.getDescription());
        holder.txtDonationCategory.setText(donation.getCategory());
        holder.txtLocation.setText(donation.getLocation());

    }

    @Override
    public int getItemCount() {
        return donationArrayList.size();
    }

    public static class DonationViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDonationTitle,txtDonationDescription,txtDonationCategory, txtLocation;
        public ImageView donationImageView;

        public DonationViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDonationTitle = itemView.findViewById(R.id.donation_title);
            txtDonationDescription = itemView.findViewById(R.id.donation_description);
            txtDonationCategory = itemView.findViewById(R.id.donation_category);
            txtLocation = itemView.findViewById(R.id.donation_location);
            donationImageView = itemView.findViewById(R.id.donation_image);

        }
    }


}
