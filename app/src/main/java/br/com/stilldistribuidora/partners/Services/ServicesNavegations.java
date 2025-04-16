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
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class ServicesNavegations extends Service {


    private static final String NOTIFICATION_CHANNEL_ID = "5554515951818";
    private Context self;

    public ServicesNavegations( ){
        self=this;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channelName = " Services..";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);
            Intent n = new Intent(self, maps.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(self, 0, n, 0);
            Notification notification = new NotificationCompat.Builder(self, NOTIFICATION_CHANNEL_ID)
                    .setContentTitle("-Services")
                    .setContentText("Ativo")
                    .setSmallIcon(R.drawable.ic_cast_dark)
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(1000, notification);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try{
                        System.out.println("Vivo ainda..!");
                        Thread.sleep(5*10);
                    }catch (Exception e){}


                }
            }
        }).start();*/
        return ServicesNavegations.START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
