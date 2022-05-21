package com.example.giveit_gi.Utils;


import androidx.annotation.Nullable;

import com.example.giveit_gi.Models.Donor;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

public class Prevalent {
    public static Donor currentloggedInDonor;


    public static final String USER_EMAIL_KEY = "EMAIL";
    public static final String USER_PASSWORD_KEY = "PASSWORD";

    public static void retrieveUserInformation(String donorID) {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        DocumentReference documentReference = firebaseFirestore.collection(CONSTANTS.DONOR_COLLECTION_PATH).document(Objects.requireNonNull(donorID));
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                currentloggedInDonor = new Donor(documentSnapshot.getString("name"),
                        documentSnapshot.getString("name"),
                        documentSnapshot.getString("name"),
                        documentSnapshot.getString("name"),
                        documentSnapshot.getString("name")
                        );
            }
        });
    }
}
