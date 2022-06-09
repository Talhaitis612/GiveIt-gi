package com.example.giveit_gi.DonorActivities.ViewHolders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.giveit_gi.DonorActivities.Interfaces.ItemClickListener;

public class DonationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView txtDonationTitle,txtDonationDescription,txtDonationCategory, txtLocation;
    public ImageView donationImageView;
    public ItemClickListener itemClickListener;
    public DonationViewHolder(@NonNull View itemView) {
        super(itemView);

    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition());
    }
}
