package com.example.smartfarm_app.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.example.smartfarm_app.R;
import com.example.smartfarm_app.controller.ui.account.AcountbeheerFragment;
import com.example.smartfarm_app.controller.ui.apparaat.bewerkenAparaatFragment;
import com.example.smartfarm_app.databinding.ActivityUserMenuBinding;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

public class UserMenu extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityUserMenuBinding binding;
    private String gebruikersnaam, wachtwoord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityUserMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarUserMenu.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_Dashboard, R.id.nav_Kasbeheer, R.id.nav_Apparaatbeheer, R.id.nav_Plantbeheer)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_user_menu);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        getGebruikerInformatie();
    }

    private void getGebruikerInformatie() {
        gebruikersnaam = SaveSharedPreference.getUserName(this);
        wachtwoord = SaveSharedPreference.getUserWachtwoord(this);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);

        TextView tvGebruikersnaam = (TextView) headerView.findViewById(R.id.tvNaamgebruiker);
        tvGebruikersnaam.setText(gebruikersnaam);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.user_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            item.getItemId();
        } else if (item.getItemId() == R.id.Sing_Out) {
            SaveSharedPreference.clearUserName(this);
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.Acount_settings) {
            // in my case I get the support fragment manager, it should work with the native one too
            FragmentManager fragmentManager = getSupportFragmentManager();
            // this will clear the back stack and displays no animation on the screen
            fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            Navigation.findNavController(this, R.id.nav_host_fragment_content_user_menu).navigate(R.id.nav_Acountbeheer);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_user_menu);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}