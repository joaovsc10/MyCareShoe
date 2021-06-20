package com.example.mycareshoe.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.mycareshoe.R;
import com.example.mycareshoe.ui.login.LoginActivity;
import com.example.mycareshoe.ui.monitoring.MonitoringFragment;
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

        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MonitoringFragment()).commit();
            navView.setCheckedItem(R.id.monitorization);

        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {


        switch (item.getItemId()){
            case R.id.monitorization:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MonitoringFragment()).commit();
                setTitle(R.string.monitorization_en);
                break;
            case R.id.statistics:
                setTitle(R.string.statistics_en);
                break;
            case R.id.settings:
                getSupportFragmentManager().beginTransaction().hide(new MonitoringFragment()).replace(R.id.fragment_container, new SettingsFragment()).commit();
                setTitle(R.string.settings_en);
                break;
            case R.id.privacy:
               setTitle(R.string.privacy_en);
                break;
            case R.id.about:
               setTitle(R.string.about_en);
                break;
            case R.id.logout:
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
