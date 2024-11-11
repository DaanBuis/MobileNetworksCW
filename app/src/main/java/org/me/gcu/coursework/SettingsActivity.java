package org.me.gcu.coursework;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;
import android.Manifest;

public class SettingsActivity extends AppCompatActivity {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 10;
    private static final String PREFS_NAME = "AppSettings";
    private static final String THEME_KEY = "theme";
    private static final String LOCATION_KEY = "location_permission";

    private Switch themeSwitch;
    private Switch locationSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the saved theme from SharedPreferences
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean isDarkTheme = prefs.getBoolean(THEME_KEY, false); // Default to light theme

        // Apply the night mode before calling setContentView()
        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        // Set the content view after the theme is set
        setContentView(R.layout.activity_settings);

        // Initialize the theme Switch
        themeSwitch = findViewById(R.id.theme_switch);
        themeSwitch.setChecked(isDarkTheme);

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save the new theme setting in SharedPreferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(THEME_KEY, isChecked);
            editor.apply();

            // Apply the new theme
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }

            // Restart the activity to apply the new theme
            recreate();  // This restarts the activity to apply the new theme
        });

        // Initialize the location Switch
        locationSwitch = findViewById(R.id.location_switch);
        boolean isLocationEnabled = prefs.getBoolean(LOCATION_KEY, false);  // Default to false (disabled)
        locationSwitch.setChecked(isLocationEnabled);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            locationSwitch.setChecked(false); // Disable the switch if permission is not granted
        }

        locationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            // Save the new location setting in SharedPreferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean(LOCATION_KEY, isChecked);
            editor.apply();

            // Handle location permission request
            if (isChecked) {
                checkLocationPermission();
            } else {
                Toast.makeText(SettingsActivity.this, "Location access disabled", Toast.LENGTH_SHORT).show();
            }
        });

        // Set up the ActionBar properties
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Set the ActionBar title (empty in your case)
            actionBar.setTitle("");

            // Display the logo in the ActionBar
            actionBar.setDisplayUseLogoEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
        }
    }

    private void checkLocationPermission() {
        // Check if the app has location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // If permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // If permission is granted, show a toast and enable location access
            Toast.makeText(SettingsActivity.this, "Location access enabled", Toast.LENGTH_SHORT).show();
        }
    }

    // Handle the result of the location permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Toast.makeText(this, "Location access enabled", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
                // Disable the location switch
                locationSwitch.setChecked(false);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle menu item clicks
        if (item.getItemId() == R.id.settings) {
            Toast.makeText(this, "You are already on the Settings Page", Toast.LENGTH_SHORT).show();
        } else if (item.getItemId() == R.id.backpage) {
            finish();  // Close the SettingsActivity
        }
        return super.onOptionsItemSelected(item);
    }
}