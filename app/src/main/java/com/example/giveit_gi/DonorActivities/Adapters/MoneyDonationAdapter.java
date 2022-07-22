package com.example.giveit_gi.DonorActivities.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.giveit_gi.DonorActivities.AuthenticationActivities.DonorLoginActivity;
import com.example.giveit_gi.DonorActivities.CRUD.UpdateDonationActivity;
import com.example.giveit_gi.DonorActivities.Interfaces.ItemClickListener;
import com.example.giveit_gi.Models.ApplyDonation;
import com.example.giveit_gi.Models.Donation;
import com.example.giveit_gi.R;
import com.example.giveit_gi.Utils.CONSTANTS;
import com.example.giveit_gi.Utils.LoadingBar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;

public class MoneyDonationAdapter extends RecyclerView.Adapter<MoneyDonationAdapter.MoneyDonationViewHolder>  {

    Context context;
    ArrayList<ApplyDonation> moneyDonationArrayList;
    FirebaseFirestore db;




    public MoneyDonationAdapter(Context context, ArrayList<ApplyDonation> moneyDonationArrayList) {
        this.context = context;
        this.moneyDonationArrayList = moneyDonationArrayList;
    }

    @NonNull
    @Override
    public MoneyDonationAdapter.MoneyDonationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.donate_money_layout, parent, false);

        return new MoneyDonationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MoneyDonationAdapter.MoneyDonationViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ApplyDonation donation = moneyDonationArrayList.get(position);
        holder.txtDonationTitle.setText(donation.getTitle());
        holder.txtDonationDescription.setText(donation.getProblem());
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a");
//        holder.appliedTime.setText(format.format(donation.getAppliedTime()));
//        holder.txtDate.setText(format.format(donation.getAppliedTime()));

//        holder.txtDate.setText(donation.getAppliedTime().toString());

        holder.txtLocation.setText(donation.getLocation());
        holder.targetAmount.setText(donation.getAmountNeeded());
        holder.txtContactNumber.setText(donation.getPhoneNo());
        holder.receivedAmount.setText(String.valueOf(donation.getAmountReceived()));
        holder.amountProgressBar.setMax(Integer.parseInt(donation.getAmountNeeded()));
        holder.amountProgressBar.setProgress((int) donation.getAmountReceived());


        holder.payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Error :", donation.toString());
                final AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.pay_dialog_layout, null);
                dialog.setView(dialogView);
                TextInputEditText amountEditText = (TextInputEditText) dialogView.findViewById(R.id.amount_editText);
                TextInputLayout amountLayout = dialogView.findViewById(R.id.amount_layout);
                Button cancelButton = dialogView.findViewById(R.id.cancelButton);
                Button confirmButton = dialogView.findViewById(R.id.payButton);
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();


                confirmButton.setOnClickListener(view12 -> {
                    String amount = amountEditText.getText().toString();

                    if (TextUtils.isEmpty(amount)) {
                        amountLayout.setError("Please enter amount...");
                    } else {
                        long amountReceived = donation.getAmountReceived();
                        amountReceived = amountReceived + Long.parseLong(amountEditText.getText().toString());
                        LoadingBar.showLoadingBar(view12.getContext(), "Wait..", context.getString(R.string.payment_processing));
                        DocumentReference documentReference = db.collection(CONSTANTS.APPLY_ITEM_COLLECTION_PATH)
                                .document(donation.getIdCardNumber());
                        documentReference.update("amountReceived", amountReceived)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            LoadingBar.hideLoadingBar();
                                            alertDialog.dismiss();
                                            Toast.makeText(context, "Payment was successfull.", Toast.LENGTH_SHORT).show();

                                        }
                                        else {
                                            LoadingBar.hideLoadingBar();
                                            alertDialog.dismiss();
                                            Toast.makeText(context, "Failed to process payment, try again later." , Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });


                    }

                });

                cancelButton.setOnClickListener(view1 -> alertDialog.dismiss());


            }
        });

    }

    @Override
    public int getItemCount() {
        return moneyDonationArrayList.size();
    }

    public static class MoneyDonationViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDonationTitle,txtDonationDescription, txtLocation, targetAmount;
        public TextView appliedTime, receivedAmount, txtContactNumber;
        public ProgressBar amountProgressBar;
        Button payButton;

        public MoneyDonationViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDonationTitle = itemView.findViewById(R.id.donation_title);
            txtDonationDescription = itemView.findViewById(R.id.donation_description);
            txtLocation = itemView.findViewById(R.id.donation_location);
            appliedTime = itemView.findViewById(R.id.appliedTime);
            targetAmount = itemView.findViewById(R.id.target_amount);
            amountProgressBar = itemView.findViewById(R.id.amountProgressBar);
            payButton = itemView.findViewById(R.id.payButton);
            receivedAmount = itemView.findViewById(R.id.start_amount);
            txtContactNumber = itemView.findViewById(R.id.contactNumber);




        }


    }


}
