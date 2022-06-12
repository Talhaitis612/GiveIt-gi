package com.example.giveit_gi.DonorActivities.Adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.text.TextUtils;
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

        holder.txtDate.setText(format.format(donation.getAppliedTime()));

        holder.txtLocation.setText(donation.getLocation());
        holder.targetAmount.setText(donation.getAmountNeeded());
        holder.amountProgressBar.setProgress(donation.getAmountReceived());
        holder.payButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View dialogView = inflater.inflate(R.layout.pay_dialog_layout, null);
                dialog.setView(dialogView);
                TextInputEditText amountEditText = (TextInputEditText) dialogView.findViewById(R.id.reset_email_edittext);
                TextInputLayout amountLayout = dialogView.findViewById(R.id.amount_layout);
                Button cancelButton = dialogView.findViewById(R.id.cancelButton);
                Button confirmButton = dialogView.findViewById(R.id.payButton);
                AlertDialog alertDialog = dialog.create();
                alertDialog.show();


                confirmButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String amount = amountEditText.getText().toString();

                        if (TextUtils.isEmpty(amount)) {
                            amountLayout.setError("Please enter amount...");
                        } else {
                            int amountReceived = donation.getAmountReceived();
                            amountReceived = amountReceived + Integer.parseInt(amountEditText.getText().toString());
                            LoadingBar.showLoadingBar(view.getContext(), "Wait..", context.getString(R.string.payment_processing));
                            Task<Void> update = db.collection(CONSTANTS.APPLY_ITEM_COLLECTION_PATH)
                                    .document(donation.getIdCardNumber())
                                    .update("amountReceived", amountReceived)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    LoadingBar.hideLoadingBar();
                                    alertDialog.dismiss();
                                    Toast.makeText(context, "Payment was successfull.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    LoadingBar.hideLoadingBar();
                                    alertDialog.dismiss();
                                    Toast.makeText(context, "Failed to process payment." + e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });


            }
        });
//        if(Objects.equals(donation.getDonorID(), CONSTANTS.currentloggedInDonor.getUid())){
//            holder.buttonLayout.setVisibility(View.VISIBLE);
//        }



//        Glide.with(context).load(donation.getImageURL()).into(holder.donationImageView);
//        holder.txtDonationTitle.setText(donation.getTitle());
//        holder.txtDonationDescription.setText(donation.getDescription());
//        holder.txtDonationCategory.setText(donation.getCategory());
//        holder.txtLocation.setText(donation.getLocation());
//        @SuppressLint({"NewApi", "LocalSuppress"})
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
//        SimpleDateFormat format = new SimpleDateFormat("EEEE, MMMM d, yyyy 'at' h:mm a");
//
//        holder.txtDate.setText(format.format(donation.getCreatedAt()));
//        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                AlertDialog.Builder builder = new AlertDialog.Builder(context);
//                builder.setMessage(context.getResources().getString(R.string.do_you_really_want_to_delete));
//                builder.setTitle(context.getResources().getString(R.string.delete));
//                builder.setCancelable(false);
//                builder.setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.cancel();
//                    }
//                });
//                builder.setPositiveButton(context.getResources().getString(R.string.sure), new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        db = FirebaseFirestore.getInstance();
//
//                        db.collection(CONSTANTS.DONATION_COLLECTION_PATH)
//                                .document(donation.getDonationID())
//                                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                                    @Override
//                                    public void onSuccess(Void unused) {
//                                        DocumentReference donorRef = db.collection(CONSTANTS.DONOR_COLLECTION_PATH).document(CONSTANTS.currentloggedInDonor.getUid());
//                                        donorRef.update("donationList", FieldValue.arrayRemove(donation.getDonationID())).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void unused) {
//                                                Toast.makeText(context, "Donation Deleted Successfully!", Toast.LENGTH_SHORT).show();
//                                            }
//                                        });
//
//                                    }
//                                })
//                                .addOnFailureListener(new OnFailureListener() {
//                                    @Override
//                                    public void onFailure(@NonNull Exception e) {
//                                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
//                                    }
//                                });
//
//
//
//                    }
//                });
//
//                AlertDialog alert = builder.create();
//                alert.show();
//            }
//        });
//        holder.updateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, UpdateDonationActivity.class);
//                intent.putExtra(CONSTANTS.DONOR_ID, donation.getDonorID());
//                intent.putExtra(CONSTANTS.DONATION_ID, donation.getDonationID());
//                intent.putExtra(CONSTANTS.DONATION_TITLE, donation.getTitle());
//                intent.putExtra(CONSTANTS.DONATION_DESCRIPTION, donation.getDescription());
//                intent.putExtra(CONSTANTS.DONATION_CATEGORY, donation.getCategory());
//                intent.putExtra(CONSTANTS.DONATION_LOCATION, donation.getLocation());
//                intent.putExtra(CONSTANTS.DONATION_IMAGE_URL, donation.getImageURL());
//                intent.putExtra(CONSTANTS.DONATION_TIME, donation.getCreatedAt());
//
//
//                context.startActivity(intent);
//            }
//        });

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                itemClickListener.onClick(view,position);
//            }
//        });

    }

    @Override
    public int getItemCount() {
        return moneyDonationArrayList.size();
    }

    public static class MoneyDonationViewHolder extends RecyclerView.ViewHolder {
        public TextView txtDonationTitle,txtDonationDescription, txtLocation, txtDate, targetAmount;
        public ProgressBar amountProgressBar;
        Button payButton;

        public MoneyDonationViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDonationTitle = itemView.findViewById(R.id.donation_title);
            txtDonationDescription = itemView.findViewById(R.id.donation_description);
            txtLocation = itemView.findViewById(R.id.donation_location);
            txtDate = itemView.findViewById(R.id.donation_time);
            targetAmount = itemView.findViewById(R.id.target_amount);
            amountProgressBar = itemView.findViewById(R.id.amountProgressBar);
            payButton = itemView.findViewById(R.id.payButton);




        }


    }


}
