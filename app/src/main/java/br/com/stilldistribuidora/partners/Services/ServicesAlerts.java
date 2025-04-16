package br.com.stilldistribuidora.partners.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.google.gson.Gson;

import br.com.stilldistribuidora.partners.Adapter.ConfigurationsUser;
import br.com.stilldistribuidora.partners.Casts.UserLoginCast;
import br.com.stilldistribuidora.partners.Contracts.OnResponseHttp;
import br.com.stilldistribuidora.partners.ServicesHttp.ServicesHttp;
import br.com.stilldistribuidora.partners.resources.ServerConfiguration;
import br.com.stilldistribuidora.partners.resources.Resources;
import br.com.stilldistribuidora.partners.views.HomeActivity;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.ConfigDataAccess;

public class ServicesAlerts extends Service {
    static final int CHANNEL_ID = Resources.CHANNEL_ID_2;
    private static final Components components = new Components();
    private Intent notificationIntent;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        components.contextServices = getApplicationContext();


        components.conf = new ServerConfiguration(components.contextServices);
        components.configurationsAdapter = new ConfigurationsUser(new ConfigDataAccess(components.contextServices));
        components.userLoginCast = new Gson().fromJson( components.conf.getUserLogged(), UserLoginCast.class);
        components.servicesHttp = new ServicesHttp(this, components.conf, new OnResponseHttp() {
            @Override
            public void onResponse(Object data) {

            }

            @Override
            public void onError(Object error) {

            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String NOTIFICATION_CHANNEL_ID = Resources.NOTIFICATION_CHANNEL_ID;
            String channelName = "Sincronização com à Still Group";
            NotificationChannel chan = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    channelName,
                    NotificationManager.IMPORTANCE_HIGH
            );

            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            assert manager != null;
            manager.createNotificationChannel(chan);

            Intent n = new Intent(components.contextServices, HomeActivity.class);
            notificationIntent=n;
            PendingIntent pendingIntent;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getActivity(
                        components.contextServices,
                        0,
                        notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE
                );
            } else {
                pendingIntent = PendingIntent.getActivity(
                    components.contextServices,
                    0,
                    notificationIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                );
            }

            Notification notification = new NotificationCompat.Builder(components.contextServices, NOTIFICATION_CHANNEL_ID)
                    .setContentTitle("Sincronização com à Still Group")
                    .setContentText("Estado PRocurando")
                    .setSmallIcon(R.drawable.ic_baseline_flip_camera_ios_24)
                    .setContentIntent(pendingIntent)
                    .build();

            startForeground(CHANNEL_ID, notification);
        }

        final Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                try {


                    System.out.println("#4 teste");
                }catch (Exception e){
                    e.printStackTrace();
                }

                if(!components.stop){
                    handler.postDelayed(this, 35000);
                }
            }
        });

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        components.stop=true;
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static class Components {
        public Context contextServices;

        public Boolean stop=false;
        public ServerConfiguration conf;
        public ConfigurationsUser configurationsAdapter;
        public ServicesHttp servicesHttp;
        public UserLoginCast userLoginCast;

    }
}
