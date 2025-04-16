package br.com.stilldistribuidora.partners.Base;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class ActivityBaseApp extends AppCompatActivity {
    /*Permisoes Gerais*/
    public void InitPermisionGeral() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.INSTANT_APP_FOREGROUND_SERVICE) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_BOOT_COMPLETED) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.INSTALL_SHORTCUT) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.LOCATION_HARDWARE) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_SETTINGS) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, android.Manifest.permission.SYSTEM_ALERT_WINDOW) != PackageManager.PERMISSION_GRANTED) {

            } else {
                String[] permissions = {
                    android.Manifest.permission.INTERNET,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION,
                    android.Manifest.permission.INSTANT_APP_FOREGROUND_SERVICE,
                    android.Manifest.permission.ACCESS_NETWORK_STATE,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_PHONE_STATE,
                    android.Manifest.permission.RECEIVE_BOOT_COMPLETED,
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.INSTALL_SHORTCUT,
                    android.Manifest.permission.LOCATION_HARDWARE,
                    android.Manifest.permission.SYSTEM_ALERT_WINDOW,
                    android.Manifest.permission.WRITE_SETTINGS,
                    android.Manifest.permission.WAKE_LOCK,
                    Manifest.permission.RECORD_AUDIO,
                    android.Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                };
                ActivityCompat.requestPermissions(this, permissions, 10000);
            }
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
