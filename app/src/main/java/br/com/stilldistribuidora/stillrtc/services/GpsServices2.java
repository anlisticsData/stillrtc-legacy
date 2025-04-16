package br.com.stilldistribuidora.stillrtc.services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.legacy.app.ActivityCompat;
import android.util.Log;

import com.google.android.gms.location.LocationCallback;

import java.util.ArrayList;
import java.util.List;

import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.Core;
import br.com.stilldistribuidora.stillrtc.db.business.DeviceStatusBusiness;
import br.com.stilldistribuidora.stillrtc.db.models.DeviceStatus;
import br.com.stilldistribuidora.stillrtc.db.models.Prefs;
import br.com.stilldistribuidora.stillrtc.utils.DateUtils;
import br.com.stilldistribuidora.stillrtc.utils.GPSTracker;
import br.com.stilldistribuidora.stillrtc.utils.PrefsHelper;
import br.com.stilldistribuidora.stillrtc.utils.Utils;

/**
 * Created by Still Technology and Development Team on 13/02/2019.
 */
public class GpsServices2 extends Service {

    public static final String TAG="#";
    private Context context;
    private static final int NOTIFICATION_ID = 20191;
    private static final Object ANDROID_CHANNEL_ID ="aaaa" ;
    public HandlerThread handlerThread;
    public Handler handler;
    public static final int TIME_DEFAULT = 1000; // 1 segundo
    private Prefs prefs;


    private LocationCallback mLocationCallback;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("#YT");

        final String deviceIdentifier;
        final  int deliveryFragmentId;
        try {

            Bundle extras = intent.getExtras();
            deviceIdentifier = extras.getString("deviceIdentifier");
            deliveryFragmentId = extras.getInt("deliveryFragmentId");

            context = GpsServices2.this;
            Log.d(TAG, "onStart");
            prefs = Core.getPrefs(context);
            if (!handlerThread.isAlive()) {
                handlerThread.start();
                handler = new Handler(handlerThread.getLooper());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final GPSTracker gps = new GPSTracker(context);
                            final DeviceStatus deviceStatus = new DeviceStatus();
                            if(lastLat != 0){
                                Log.e(TAG,"[Device Status] lastLat " + String.valueOf(lastLat));
                                deviceStatus.setId(0);
                                deviceStatus.setLatitude(lastLat);
                                deviceStatus.setLongitude(lastLng);
                                deviceStatus.setBatery(Utils.getBatteryPercentage(context));
                                deviceStatus.setIp(Utils.getIPAddress(true));
                                deviceStatus.setDeviceIdentifier(deviceIdentifier);
                                deviceStatus.setDeviceFragmentId(deliveryFragmentId);
                                deviceStatus.setCreatedAt(DateUtils.recuperarDateTimeAtual());

                            } else {

                                deviceStatus.setId(0);
                                deviceStatus.setLatitude(gps.getLatitude());
                                deviceStatus.setLongitude(gps.getLongitude());
                                deviceStatus.setBatery(Utils.getBatteryPercentage(context));
                                deviceStatus.setIp(Utils.getIPAddress(true));
                                deviceStatus.setDeviceIdentifier(deviceIdentifier);
                                deviceStatus.setDeviceFragmentId(deliveryFragmentId);
                                deviceStatus.setCreatedAt(DateUtils.recuperarDateTimeAtual());
                            }
                            if(Utils.isNetworkAvailable(context) && Utils.isOnline()){
                                if(deviceStatus.getLatitude()!= 0 && deviceStatus.getLongitude()!=0){
                                    Core.registrarDispositivoStatusWS(context, deviceStatus);
                                    Log.e(TAG,"*EE[Device Status] s  "+deviceStatus.getLatitude() +"  = "+deviceStatus.getLongitude());
                                } else {
                                    Log.e(TAG,"*EE[Device Status] Erro de GPS! A latitude/longitude estão invalidos");
                                }
                                verificarDispositivoStatus(context);
                            } else {
                                if(deviceStatus.getLatitude()!= 0 && deviceStatus.getLongitude()!=0) {
                                    DeviceStatusBusiness deviceStatusBusiness = new DeviceStatusBusiness(context);
                                    deviceStatusBusiness.insert(deviceStatus);
                                } else {
                                    Log.e(TAG,"*[Device Status] Erro de GPS! A latitude/longitude estão invalidos");
                                }
                            }
                            handler.postDelayed(this, (TIME_DEFAULT * prefs.getTimeLoopPoints()));

                        } catch (Exception execution){
                            Log.e(TAG,"*Error in register device status! x(");
                        }
                    }
                };
                handler.post(runnable);
            }


        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }




        return  START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    @Override
    public void onCreate() {



        handlerThread = new HandlerThread("HandlerThread");

        Log.e(TAG, "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }

        PrefsHelper prefsHelper = new PrefsHelper(getApplicationContext());

        setDeviceIdentifier(prefsHelper.getPref(PrefsHelper.PREF_DEVICE_IDENTIFIER).toString());
        setDeliveryFragmentId(Integer.parseInt(prefsHelper.getPref(PrefsHelper.PREF_TEMP_OPERATION_ID).toString()));




    }

    @Override
    public void onDestroy() {


        super.onDestroy();




        handlerThread.quit();

        Log.e( TAG, "onDestroy" );
        if (mLocationManager != null) {
            for (GpsServices2.LocationListener mLocationListener : mLocationListeners) {
                try {
                    if (ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission( this, Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLocationManager.removeUpdates( mLocationListener );
                } catch (Exception ex) {
                    Log.i( TAG, "fail to remove location listners, ignore", ex );
                }
            }
        }




    }

    public double lastLat;
    public double lastLng;

    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 5f;



    private class LocationListener implements android.location.LocationListener{
        Location mLastLocation;
        LocationListener(String provider)
        {
            mLastLocation = new Location(provider);
            lastLat = mLastLocation.getLatitude();
            lastLng = mLastLocation.getLongitude();



        }
        @Override
        public void onLocationChanged(Location location)
        {
            Log.e(TAG, "onLocationChanged: lat: " + mLastLocation.getLatitude() + " lng:"+mLastLocation.getLongitude());
            mLastLocation.set(location);
            lastLat = mLastLocation.getLatitude();
            lastLng = mLastLocation.getLongitude();



        }
        @Override
        public void onProviderDisabled(String provider)
        {
            //Log.e(TAG, "onProviderDisabled: " + provider);
        }
        @Override
        public void onProviderEnabled(String provider)
        {
            //Log.e(TAG, "onProviderEnabled: " + provider);
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
            // Log.e(TAG, "onStatusChanged: " + provider);
        }
    }
    GpsServices2.LocationListener[] mLocationListeners = new GpsServices2.LocationListener[] {
            new GpsServices2.LocationListener(LocationManager.GPS_PROVIDER),
            new GpsServices2.LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public String deviceIdentifier;
    public int deliveryFragmentId;

    public int getDeliveryFragmentId() {
        return deliveryFragmentId;
    }

    public void setDeliveryFragmentId(int deliveryFragmentId) {
        this.deliveryFragmentId = deliveryFragmentId;
    }

    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }

    public void setDeviceIdentifier(String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
    }

    public void verificarDispositivoStatus(Context context){
        DeviceStatusBusiness deviceStatusBusiness = new DeviceStatusBusiness(context);
        List<DeviceStatus> _list = (ArrayList<DeviceStatus>) deviceStatusBusiness.getList(Constants.KEY_SYNC+" = 0","");

        if(_list.size()>0){
            for(DeviceStatus deviceStatus: _list){
                Core.registrarDispositivoStatusWS(context, deviceStatus);
            }
        }
    }


}
