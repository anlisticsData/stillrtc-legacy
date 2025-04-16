package br.com.stilldistribuidora.stillrtc.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;

public class ManagerServices extends Service {
    public static final String TAG = ServiceDeviceStatus.class.getSimpleName();
    public static final int TIME_DEFAULT = 1000; // 1 segundo
    private static final int NOTIFICATION_ID = 6525112;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 5f;



    private Context context;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = getApplicationContext();
        return super.onStartCommand(intent, flags, startId);
    }
}
