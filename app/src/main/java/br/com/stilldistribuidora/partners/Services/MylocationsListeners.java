package br.com.stilldistribuidora.partners.Services;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

import androidx.annotation.NonNull;

public class MylocationsListeners implements LocationListener {
    public  static  double latitude,logitude;

    @Override
    public void onLocationChanged(@NonNull Location location) {
        this.latitude=location.getLatitude();
        this.logitude=location.getLongitude();


    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}
