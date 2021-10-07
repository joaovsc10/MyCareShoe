package com.example.mycareshoe.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.mycareshoe.ui.statistics.StatisticsFragment;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private SettingsFragment settingsFragment = new SettingsFragment();
    private MonitoringFragment monitoringFragment = new MonitoringFragment();
    private StatisticsFragment statisticsFragment = new StatisticsFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {



    /*    //Get the text file
        File dir = new File(Environment.getExternalStorageDirectory().toString() + "/Download");
        dir.mkdir();
        File file = new File(dir, "BLABLABLAS.txt");


        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            if(file.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String line;

                while ((line = br.readLine()) != null) {
                    text.append(line);
                    text.append('\n');
                }
                br.close();
            }
        }
        catch (IOException e) {
            System.out.println(e);
        }
*/
        super.onCreate(savedInstanceState);


        if (savedInstanceState != null) {
            monitoringFragment = (MonitoringFragment) getSupportFragmentManager().getFragment(savedInstanceState, "monitorization");
            settingsFragment = (SettingsFragment) getSupportFragmentManager().getFragment(savedInstanceState, "settings");
            statisticsFragment = (StatisticsFragment) getSupportFragmentManager().getFragment(savedInstanceState, "statistics");

        }
        setContentView(R.layout.sidebar);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);


        NavigationView navView = findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
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

                TextView textViewName = findViewById(R.id.name);
                TextView textViewEmail = findViewById(R.id.email);
                textViewName.setText(SharedPrefManager.getInstance(getApplicationContext()).getPatient(false).getUsername().toString());
                textViewEmail.setText(SharedPrefManager.getInstance(getApplicationContext()).getPatient(false).getEmail().toString());
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


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, monitoringFragment).commit();
            navView.setCheckedItem(R.id.monitorization);

        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.monitorization:

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, monitoringFragment).addToBackStack("monitorization").commit();
                setTitle(R.string.monitorization_en);
                break;
            case R.id.statistics:

                if (statisticsFragment == null)
                    statisticsFragment = new StatisticsFragment();
                getSupportFragmentManager().beginTransaction().hide(new MonitoringFragment()).replace(R.id.fragment_container, statisticsFragment).addToBackStack("statistics").commit();
                setTitle(R.string.statistics_en);
                break;
            case R.id.settings:

                if (settingsFragment == null)
                    settingsFragment = new SettingsFragment();
                getSupportFragmentManager().beginTransaction().hide(new MonitoringFragment()).replace(R.id.fragment_container, settingsFragment).addToBackStack("settings").commit();
                setTitle(R.string.settings_en);
                break;
            case R.id.personalInfo:
                getSupportFragmentManager().beginTransaction().hide(new MonitoringFragment()).replace(R.id.fragment_container, new PersonalInfoFragment()).addToBackStack("personal_info").commit();
                setTitle(R.string.personalInfo_en);
                break;
            case R.id.logout:
                SharedPrefManager.getInstance(getApplicationContext()).logout();
                Intent intent = new Intent(this, LoginActivity.class);
                if (settingsFragment != null) {
                    if (settingsFragment.bluetoothFragment != null) {
                        if (settingsFragment.bluetoothFragment.getBluetoothControllerRight() != null && settingsFragment.bluetoothFragment.getBluetoothControllerLeft() != null) {
                            if (settingsFragment.bluetoothFragment.getBluetoothControllerRight().getRightSocket() != null || settingsFragment.bluetoothFragment.getBluetoothControllerLeft().getLeftSocket() != null) {
                                try {
                                    settingsFragment.bluetoothFragment.getBluetoothControllerRight().stop("R");

                                    settingsFragment.bluetoothFragment.getBluetoothControllerLeft().stop("L");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
                startActivity(intent);
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "monitorization", monitoringFragment);
        if (getSupportFragmentManager().getFragment(outState, "settings") != null)
            getSupportFragmentManager().putFragment(outState, "settings", settingsFragment);

        if (getSupportFragmentManager().getFragment(outState, "statistics") != null)
            getSupportFragmentManager().putFragment(outState, "statistics", statisticsFragment);
    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


}
