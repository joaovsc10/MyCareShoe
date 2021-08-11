package com.example.mycareshoe.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.mycareshoe.R;
import com.example.mycareshoe.helpers.SharedPrefManager;
import com.example.mycareshoe.ui.login.LoginActivity;
import com.example.mycareshoe.ui.monitoring.MonitoringFragment;
import com.example.mycareshoe.ui.personalInfo.PersonalInfoFragment;
import com.example.mycareshoe.ui.settings.SettingsFragment;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sidebar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer= findViewById(R.id.drawer_layout);


        NavigationView navView= findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigations_drawer_open, R.string.navigations_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();





        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //Called when a drawer's position changes.

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //Called when a drawer has settled in a completely open state.
                //The drawer is interactive at this point.
                // If you have 2 drawers (left and right) you can distinguish
                // them by using id of the drawerView. int id = drawerView.getId();
                // id will be your layout's id: for example R.id.left_drawer
                TextView textViewName = findViewById(R.id.name);
                TextView textViewEmail = findViewById(R.id.email);
                textViewName.setText(SharedPrefManager.getInstance(getApplicationContext()).getUser(false).getDisplayName().toString());
                textViewEmail.setText(SharedPrefManager.getInstance(getApplicationContext()).getUser(false).getEmail().toString());
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // Called when a drawer has settled in a completely closed state.
            }



            @Override
            public void onDrawerStateChanged(int newState) {
                // Called when the drawer motion state changes. The new state will be one of STATE_IDLE, STATE_DRAGGING or STATE_SETTLING.
            }
        });






        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MonitoringFragment()).commit();
            navView.setCheckedItem(R.id.monitorization);

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {


        switch (item.getItemId()){
            case R.id.monitorization:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MonitoringFragment()).addToBackStack("monitorization").commit();
                setTitle(R.string.monitorization_en);
                break;
            case R.id.statistics:
                setTitle(R.string.statistics_en);
                break;
            case R.id.settings:
                getSupportFragmentManager().beginTransaction().hide(new MonitoringFragment()).replace(R.id.fragment_container, new SettingsFragment()).addToBackStack("settings").commit();
                setTitle(R.string.settings_en);
                break;
            case R.id.privacy:
               setTitle(R.string.privacy_en);
                break;
            case R.id.about:
               setTitle(R.string.about_en);
                break;
            case R.id.personalInfo:
                getSupportFragmentManager().beginTransaction().hide(new MonitoringFragment()).replace(R.id.fragment_container, new PersonalInfoFragment()).addToBackStack("personal_info").commit();
                setTitle(R.string.personalInfo_en);
                break;
            case R.id.logout:
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {

        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

}
