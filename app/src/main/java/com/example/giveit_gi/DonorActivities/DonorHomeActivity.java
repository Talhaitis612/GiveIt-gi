package com.example.giveit_gi.DonorActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.giveit_gi.MainActivity;
import com.example.giveit_gi.Models.Donor;
import com.example.giveit_gi.R;
import com.example.giveit_gi.Utils.CONSTANTS;
import com.example.giveit_gi.databinding.ActivityDonorHomeBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.Objects;

import io.paperdb.Paper;

public class DonorHomeActivity extends AppCompatActivity {
    private ActivityDonorHomeBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    FirebaseAuth mAuth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDonorHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Paper.init(this);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.nav_open, R.string.nav_close);
        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        mAuth = FirebaseAuth.getInstance();

        String currentId= mAuth.getCurrentUser().getUid();
        View headerView = binding.navView.getHeaderView(0);
        TextView nameEdTextView = (TextView) headerView.findViewById(R.id.user_name);

        db = FirebaseFirestore.getInstance();
        db.collection(CONSTANTS.DONOR_COLLECTION_PATH).document(currentId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                CONSTANTS.currentloggedInDonor = new Donor(documentSnapshot.getString("name"),
                        documentSnapshot.getString("email"),
                        documentSnapshot.getString("phone"),
                        "",
                        documentSnapshot.getString("profilePicture")
                );
                nameEdTextView.setText(CONSTANTS.currentloggedInDonor.getName());

            }
        });



        // to make the Navigation drawer icon always appear on the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Home");
        binding.navView.setNavigationItemSelectedListener(this::onOptionsItemSelected);


    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        int id = item.getItemId();


         if (id == R.id.nav_category) {

        }
        else if (id == R.id.nav_settings) {
//            Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
//            startActivity(intent);

        }
        else if (id == R.id.nav_logout) {
            Log.i("Talha", "This is getting selected!");
            Paper.book().destroy();
            Intent intent = new Intent(DonorHomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }



        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}