package com.example.prachisdemo.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.prachisdemo.DialogActivity;
import com.example.prachisdemo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class DashboardActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.txt_nav_profile) {

            Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_user) {

            Intent intent = new Intent(DashboardActivity.this, UserListActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_logout) {
            clearSharedPref(DashboardActivity.this);
            Intent intent = new Intent(DashboardActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_battery_indicator) {
            Intent intent = new Intent(DashboardActivity.this, BatteryIndicatorActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_contect) {
            Intent intent = new Intent(DashboardActivity.this, ContactListActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_music) {
            Intent intent = new Intent(DashboardActivity.this, MyServicesActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_map) {
            Intent intent = new Intent(DashboardActivity.this, MapsActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_myfirebasemessagingservice) {
            Intent intent = new Intent(DashboardActivity.this, MyFirebaseMessagingService.class);
            startActivity(intent);

        } else if (id == R.id.nav_webservices) {
            Intent intent = new Intent(DashboardActivity.this, WebServicesActivity.class);
            startActivity(intent);

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dashboard, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // action with ID action_settings was selected
            case R.id.item_settings:
                Toast.makeText(DashboardActivity.this, "Settings selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            // action with ID action_logout was selected
            case R.id.item_logout:
                clearSharedPref(DashboardActivity.this);
                Intent intent = new Intent(DashboardActivity.this, SignInActivity.class);
                startActivity(intent);
                Toast.makeText(DashboardActivity.this, "Logout selected", Toast.LENGTH_SHORT)
                        .show();
                finish();
                break;
            // action with ID action_alter_delete was selected
            case R.id.item_delete:
                Intent intent1 = new Intent(DashboardActivity.this, DialogActivity.class);
                startActivity(intent1);
                Toast.makeText(this, "Delete Selected", Toast.LENGTH_SHORT).show();
            default:
                break;
        }

        return true;
    }


    private void clearSharedPref(DashboardActivity dashboardActivity) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

}
