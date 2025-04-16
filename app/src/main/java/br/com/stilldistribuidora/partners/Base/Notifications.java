package br.com.stilldistribuidora.partners.Base;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Random;

import br.com.stilldistribuidora.stillrtc.R;

public class Notifications  extends AppCompatActivity {

    private static  String ID_NOTIFICATION  ;
    private static final int INT_ID_NOTIFICATION = 1 ;
    private static final CharSequence NOTIFICATIONS_NAME ="Notification" ;
    private final Context activyContext;
    private static NotificationManagerCompat notificationManagerCompat;
    private  static Notification notification;
    private String titleDefault="System Informations.";
    private boolean hasNotificationActiove=false;


    
    public Notifications(Context activyContext){
        this.activyContext =  activyContext;
        Random gerador = new Random();
        ID_NOTIFICATION=String.valueOf(gerador.nextInt(26));
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(ID_NOTIFICATION,NOTIFICATIONS_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager =this.activyContext.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }
    public void Notificar(String message,String... title) throws Exception{
        String alertTitle=(title.length==0) ? this.titleDefault: title[0];
        NotificationCompat.Builder builder   = new NotificationCompat.Builder(this.activyContext,ID_NOTIFICATION)
                .setSmallIcon(R.drawable.ic_baseline_notifications_active_24)
                .setContentTitle(alertTitle)
                .setContentText(message);
        try{ notificationManagerCompat.cancel(INT_ID_NOTIFICATION);}catch(Exception e){}
        notification = builder.build();
        notificationManagerCompat=NotificationManagerCompat.from(this.activyContext);
        notificationManagerCompat.notify(INT_ID_NOTIFICATION,notification);
        this.hasNotificationActiove=true;



    }

    public boolean isHasNotificationActive() {
        return hasNotificationActiove;
    }

    public void delete() throws Exception {
        notificationManagerCompat.cancel(INT_ID_NOTIFICATION);
        this.hasNotificationActiove=false;
    }
}
