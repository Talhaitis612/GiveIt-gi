package com.example.giveit_gi.DonorActivities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.giveit_gi.MainActivity;
import com.example.giveit_gi.Models.Donor;
import com.example.giveit_gi.R;
import com.example.giveit_gi.Utils.CONSTANTS;
import com.example.giveit_gi.databinding.ActivityDonorHomeBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;
import java.util.Objects;

import io.paperdb.Paper;

public class DonorHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ActivityDonorHomeBinding binding;
    private AppBarConfiguration mAppBarConfiguration;
    FirebaseAuth mAuth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDonorHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.navView.setNavigationItemSelectedListener(this);
        setSupportActionBar(binding.appBarHome.toolbar);

        Paper.init(this);



        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



        mAuth = FirebaseAuth.getInstance();

        String currentId= mAuth.getCurrentUser().getUid();
        View headerView = binding.navView.getHeaderView(0);
        TextView nameEdTextView = (TextView) headerView.findViewById(R.id.user_name);
        ImageView profileImageView = (ImageView) headerView.findViewById(R.id.user_profile_image);

        db = FirebaseFirestore.getInstance();
        db.collection(CONSTANTS.DONOR_COLLECTION_PATH).document(currentId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                assert documentSnapshot != null;

                ArrayList<String> donationList = (ArrayList<String>) documentSnapshot.get("donationList");
                ArrayList<String> eventList = (ArrayList<String>) documentSnapshot.get("eventList");
                ArrayList<String> moneyContributionList = (ArrayList<String>) documentSnapshot.get("moneyContributionList");


                CONSTANTS.currentloggedInDonor = new
                        Donor(
                        currentId,
                        documentSnapshot.getString("name"),
                        documentSnapshot.getString("email"),
                        documentSnapshot.getString("phone"),
                        documentSnapshot.getString("password"),
                        documentSnapshot.getString("profilePicture"),
                        donationList,
                        eventList,
                        moneyContributionList
                );
//                nameEdTextView.setText(CONSTANTS.currentloggedInDonor.getName());

                nameEdTextView.setText(CONSTANTS.currentloggedInDonor.getName());
                if(Objects.equals(CONSTANTS.currentloggedInDonor.getProfilePicture(), "")){
                    return;
                }
                Glide.with(DonorHomeActivity.this).load(CONSTANTS.currentloggedInDonor.getProfilePicture())
                        .into(profileImageView);

            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.more_options, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.nav_logout) {// do whatever
            Paper.book().destroy();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(DonorHomeActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }



    @Override
    protected void onStart() {
        super.onStart();


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.nav_yourHistory){
            finish();
        }

        return false;
    }

    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
}