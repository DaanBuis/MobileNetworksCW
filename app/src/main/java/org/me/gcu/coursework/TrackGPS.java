package org.me.gcu.coursework;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;


public class TrackGPS extends Service implements LocationListener {

    private final Context ctxt;

    boolean checkGPS = false;

    Location myLocation;
    protected LocationManager locationManager;
    double latitude;
    double longitude;
    double altitude;

    private static final long MINDELAY = 1000 * 60;
    private static final long MINDISTANCE = 10;

    public TrackGPS(Context ctxt) {
        this.ctxt = ctxt;
        getLocation();
    }

    public double getLatitude() {
        if (myLocation != null) return myLocation.getLatitude();
        return latitude;
    }

    public double getAltitude() {
        if (myLocation != null) return myLocation.getAltitude();
        return altitude;
    }

    public double getLongitude() {
        if (myLocation != null) return myLocation.getLongitude();
        return longitude;
    }

    public boolean canGetLocation() { return this.checkGPS; }


    private Location getLocation() {
        try{
            if (ContextCompat.checkSelfPermission(ctxt, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(ctxt, "Location permission denied. Cannot access GPS data.", Toast.LENGTH_SHORT).show();
                return null;

            }
            locationManager = (LocationManager) ctxt.getSystemService(LOCATION_SERVICE);

            checkGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            if(checkGPS) {
                try{
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MINDELAY, MINDISTANCE, this);
                    if (locationManager != null) {
                        myLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (myLocation != null) {
                            latitude = myLocation.getLatitude();
                            longitude = myLocation.getLongitude();
                            altitude = myLocation.getAltitude();
                        }
                    }
                } catch (SecurityException e) {
                    Toast.makeText(ctxt, "No permission to access GPS", Toast.LENGTH_SHORT).show();
                }
            }
            else {
                Toast.makeText(ctxt, "No service provider available", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myLocation;
    }

    public void showAlert() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(ctxt);
        dialog.setTitle("GPS Disabled");
        dialog.setMessage("Do you want to turn on GPS?");
        dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                ctxt.startActivity(intent);
            }
        });
        dialog.setNegativeButton("NO",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        dialog.show();
    }

    public void stopGPS() {
        if (locationManager != null) {
            try {
                locationManager.removeUpdates(TrackGPS.this);
            } catch (SecurityException e) {
                Toast.makeText(ctxt,"No permission to access GPS",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(@NonNull Location location){

    }
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }



}