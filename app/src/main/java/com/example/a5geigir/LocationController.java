package com.example.a5geigir;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class LocationController {

    private Context context;
    private static LocationController instance = null;
    private Location ubi;
    private FusedLocationProviderClient fusedLocationClient;

    private LocationController(Context ctx) {

        context = ctx;

        ubi = new Location("");
        ubi.setLatitude(0);
        ubi.setLongitude(0);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        getLastLocation();

    }

    public static LocationController getInstance(Context context){
        if (instance == null)
            instance = new LocationController(context);
        return instance;
    }

    public Location getLastLocation(){

        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.getCurrentLocation(LocationRequest.QUALITY_HIGH_ACCURACY,null).addOnSuccessListener((Activity) context, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    ubi = location;
                }
            });
        }

        return ubi;
    }

}
