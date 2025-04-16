package br.com.stilldistribuidora.stillrtc.services;

 import android.location.Location;
 import android.os.Bundle;

import androidx.annotation.NonNull;

import com.google.android.gms.location.LocationListener;

public class MinhaLocalizacaoListener implements LocationListener {
    public static double latitude;
    public static double longitude;

    @Override
    public void onLocationChanged(Location location) {
        this.latitude  = location.getLatitude();
        this.longitude = location.getLongitude();
    }


}