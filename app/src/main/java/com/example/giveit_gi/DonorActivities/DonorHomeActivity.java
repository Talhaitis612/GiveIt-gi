package com.example.giveit_gi.DonorActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.giveit_gi.MainActivity;
import com.example.giveit_gi.R;
import com.example.giveit_gi.Utils.Prevalent;
import com.example.giveit_gi.databinding.ActivityDonorHomeBinding;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

import io.paperdb.Paper;

public class DonorHomeActivity extends AppCompatActivity {
    private ActivityDonorHomeBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    FirebaseAuth mAuth;
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
        nameEdTextView.setText(Prevalent.currentloggedInDonor.getName());

        // to make the Navigation drawer icon always appear on the action bar
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Home");
        binding.navView.setNavigationItemSelectedListener(this::onOptionsItemSelected);
//        if(!Objects.isNull(Prevalent.currentloggedInDonor)){
//            TextView nameEditText = (TextView) findViewById(R.id.user_name);
//            nameEditText.setText(Prevalent.currentloggedInDonor.getName());
//        }


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
}