package br.com.stilldistribuidora.stillrtc.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import androidx.legacy.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.Core;
import br.com.stilldistribuidora.stillrtc.db.business.DeviceStatusBusiness;
import br.com.stilldistribuidora.stillrtc.db.models.DeviceStatus;
import br.com.stilldistribuidora.stillrtc.db.models.Prefs;
import br.com.stilldistribuidora.stillrtc.ui.activities.MapsActivity;
import br.com.stilldistribuidora.stillrtc.utils.DateUtils;
import br.com.stilldistribuidora.stillrtc.utils.GPSTracker;
import br.com.stilldistribuidora.stillrtc.utils.PrefsHelper;
import br.com.stilldistribuidora.stillrtc.utils.Utils;


public class ServiceDeviceStatus extends Service {

    public static final String TAG = ServiceDeviceStatus.class.getSimpleName();
    public static final int TIME_DEFAULT = 1000; // 1 segundo
    private static final int NOTIFICATION_ID = 20191;
    private static final Object ANDROID_CHANNEL_ID = "aaaa";
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 5f;
    public HandlerThread handlerThread;
    public Handler handler;
    public double lastLat;
    public double lastLng;
    public String deviceIdentifier;
    public int deliveryFragmentId;
    /*
    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };
    */


    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER)
    };

    private Prefs prefs;
    private Context context;
    private NotificationManager manager;
    private LocationManager mLocationManager = null;
    public ServiceDeviceStatus() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        System.out.println("#5 Iniciando Serviso");


        try {
            context = ServiceDeviceStatus.this;
            Bundle extras = intent.getExtras();
            deviceIdentifier = extras.getString("deviceIdentifier");
            deliveryFragmentId = extras.getInt("deliveryFragmentId");
            Log.d(TAG, "onStart");
            prefs = Core.getPrefs(context);
            if (!handlerThread.isAlive()) {
                handlerThread.start();
                handler = new Handler(handlerThread.getLooper());
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("#5 Iniciando Serviso Thread "+deviceIdentifier+"  = > "+deliveryFragmentId);
                        try {
                            final GPSTracker gps = new GPSTracker(context);
                            final DeviceStatus deviceStatus = new DeviceStatus();
                            if (lastLat != 0) {
                                Log.e(TAG, "[Device Status] lastLat " + lastLat);
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
                            if (Utils.isNetworkAvailable(context) && Utils.isOnline()) {

                                System.out.println("#5 Enviando Ponto "+deviceIdentifier+"  = > "+deliveryFragmentId);
                                if (deviceStatus.getLatitude() != 0 && deviceStatus.getLongitude() != 0) {

                                    Core.registrarDispositivoStatusWS(context, deviceStatus);
                                    Log.e(TAG, "[Device Status] s  " + deviceStatus.getLatitude() + "  = " + deviceStatus.getLongitude());
                                } else {
                                    Log.e(TAG, "[Device Status] Erro de GPS! A latitude/longitude estão invalidos");
                                }
                                verificarDispositivoStatus(context);
                            } else {
                                if (deviceStatus.getLatitude() != 0 && deviceStatus.getLongitude() != 0) {
                                    System.out.println("#5 SALVANDO LOCAL "+deviceIdentifier+"  = > "+deliveryFragmentId);
                                    DeviceStatusBusiness deviceStatusBusiness = new DeviceStatusBusiness(context);
                                    deviceStatusBusiness.insert(deviceStatus);
                                } else {
                                    Log.e(TAG, "[Device Status] Erro de GPS! A latitude/longitude estão invalidos");
                                }



                            }
                            handler.postDelayed(this, (TIME_DEFAULT * prefs.getTimeLoopPoints()));

                        } catch (Exception execution) {
                            Log.e(TAG, "Error in register device status! x(");
                        }
                    }
                };
                handler.post(runnable);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    String NOTIFICATION_CHANNEL_ID = "123";
                    String channelName = "My Background Service";
                    NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
                    chan.setLightColor(Color.BLUE);
                    chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                    manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    assert manager != null;
                    manager.createNotificationChannel(chan);
                    Intent n = new Intent(context, MapsActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, n, 0);
                    Notification notification = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                            .setContentTitle("Rtc-gps")
                            .setContentText("Ativo")
                            .setSmallIcon(R.drawable.ic_dispositivo_online)
                            .setContentIntent(pendingIntent)
                            .build();
                    startForeground(1, notification);
                }
            }
       } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return Service.START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        handlerThread = new HandlerThread("HandlerThread");
        Log.e(TAG, "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }

        PrefsHelper prefsHelper = new PrefsHelper(getApplicationContext());

        setDeviceIdentifier(prefsHelper.getPref(PrefsHelper.PREF_DEVICE_IDENTIFIER).toString());
        setDeliveryFragmentId(Integer.parseInt(prefsHelper.getPref(PrefsHelper.PREF_TEMP_OPERATION_ID).toString()));


    }

    @SuppressLint("MissingPermission")
    @Override
    public void onDestroy() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.deleteNotificationChannel("123");
        }


        super.onDestroy();

        handlerThread.quit();
        Log.e(TAG, "onDestroy");
        if (mLocationManager != null) {
            for (LocationListener mLocationListener : mLocationListeners) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListener);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }


    }

    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

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

    public void verificarDispositivoStatus(Context context) {
        DeviceStatusBusiness deviceStatusBusiness = new DeviceStatusBusiness(context);
        List<DeviceStatus> _list = (ArrayList<DeviceStatus>) deviceStatusBusiness.getList(Constants.KEY_SYNC + " = 0", "");

        if (_list.size() > 0) {
            for (DeviceStatus deviceStatus : _list) {
                Core.registrarDispositivoStatusWS(context, deviceStatus);
            }
        }
    }

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        LocationListener(String provider) {
            mLastLocation = new Location(provider);
            lastLat = mLastLocation.getLatitude();
            lastLng = mLastLocation.getLongitude();


        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: lat: " + mLastLocation.getLatitude() + " lng:" + mLastLocation.getLongitude());
            mLastLocation.set(location);
            lastLat = mLastLocation.getLatitude();
            lastLng = mLastLocation.getLongitude();


        }

        @Override
        public void onProviderDisabled(String provider) {
            //Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            //Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // Log.e(TAG, "onStatusChanged: " + provider);
        }
    }


}