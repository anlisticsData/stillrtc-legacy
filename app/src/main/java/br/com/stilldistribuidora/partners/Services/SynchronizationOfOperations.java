package br.com.stilldistribuidora.partners.Services;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class SynchronizationOfOperations extends Service {
    private static final Components components = new Components();

    public SynchronizationOfOperations() {
        components.loop = true;
        components.context=getBaseContext();
    }


    @Nullable
    @Override
    public ComponentName startForegroundService(Intent service) {
        return super.startForegroundService(service);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        new Thread(new Runnable() {
            public void run() {
                // TODO Auto-generated method stub
                while (components.loop) {
                    try {
                        Log.d("#14", "@3s");
                        Thread.sleep(3000);
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

            }
        }).start();

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        components.loop = false;
        super.onDestroy();

    }


    private static class Components {
        public boolean loop;
        public Context context;
    }
}
