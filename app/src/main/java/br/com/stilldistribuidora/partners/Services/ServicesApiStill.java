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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.stilldistribuidora.partners.Adapter.ConfigurationsUser;
import br.com.stilldistribuidora.partners.Base.Playler;
import br.com.stilldistribuidora.partners.Base.navegador.DeliveryStateReaderDbHelper;
import br.com.stilldistribuidora.partners.Base.routerMovimentDomain.RouterMovimentBusiness;
import br.com.stilldistribuidora.partners.Base.routerMovimentDomain.RouterMovimentHelper;
import br.com.stilldistribuidora.partners.Casts.UserLoginCast;
import br.com.stilldistribuidora.partners.Commom.Enum;
import br.com.stilldistribuidora.partners.Contracts.OnResponseHttp;
import br.com.stilldistribuidora.partners.Repository.RepositoryConcret.AppOpeationsRepository;
import br.com.stilldistribuidora.partners.Repository.RepositoryModels.OperationModelRepository;
import br.com.stilldistribuidora.partners.ServicesHttp.ServicesHttp;
import br.com.stilldistribuidora.partners.resources.ServerConfiguration;
import br.com.stilldistribuidora.partners.resources.Resources;
import br.com.stilldistribuidora.partners.views.HomeActivity;
import br.com.stilldistribuidora.partners.views.core.models.PhotosPartnersModel;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.ConfigDataAccess;

public class ServicesApiStill extends Service {
    static final int CHANNEL_ID = Resources.CHANNEL_ID;
    private static final ContextFrame ContextFrame = new ContextFrame();
    private Intent notificationIntent;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ContextFrame.contextServices = getApplicationContext();
        ContextFrame.operationRepository = new AppOpeationsRepository(ContextFrame.contextServices);
        ContextFrame.photosRepository = new PhotosPartnersModel(ContextFrame.contextServices);
        ContextFrame.deliveryStateRepository= new DeliveryStateReaderDbHelper(ContextFrame.contextServices);
        ContextFrame.routerMovimentBusiness=new RouterMovimentBusiness(new RouterMovimentHelper(ContextFrame.contextServices));

        ContextFrame.conf = new ServerConfiguration(ContextFrame.contextServices);
        ContextFrame.configurationsAdapter = new ConfigurationsUser(new ConfigDataAccess(ContextFrame.contextServices));
        ContextFrame.userLoginCast = new Gson().fromJson( ContextFrame.conf.getUserLogged(), UserLoginCast.class);
        ContextFrame.servicesHttp = new ServicesHttp(this, ContextFrame.conf, new OnResponseHttp() {
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

            Intent n = new Intent(ContextFrame.contextServices, HomeActivity.class);
            notificationIntent=n;
            PendingIntent pendingIntent;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getActivity(
                        ContextFrame.contextServices,
                        0,
                        notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE
                );
            } else {
                pendingIntent = PendingIntent.getActivity(
                    ContextFrame.contextServices,
                    0,
                    notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
            }

            Notification notification = new NotificationCompat.Builder(ContextFrame.contextServices, NOTIFICATION_CHANNEL_ID)
                    .setContentTitle("Sincronização com à Still Group")
                    .setContentText("Estado ativo")
                    .setSmallIcon(R.drawable.ic_baseline_flip_camera_ios_24)
                    .setContentIntent(pendingIntent)
                    .build();

            startForeground(CHANNEL_ID, notification);
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                Date dataHoraAtual = new Date();
                String data = new SimpleDateFormat("yyyy-MM-dd").format(dataHoraAtual);
                AppOpeationsRepository operationModel = new AppOpeationsRepository(ContextFrame.contextServices);
                List<OperationModelRepository> operationsData = operationModel.getAll(String.valueOf(Enum.STATUS_IS_OPEN_OPERATION));
                for (OperationModelRepository p : operationsData){
                    if (!p.getCreatedAt().contains(data)) {
                        operationModel.updateStateOperations(String.valueOf(p.getOperationID()),ContextFrame.userLoginCast.getUuid(),Enum.STATUS_CLOSE_SAVE_DEVICE);
                    }
                }
            }
        }).start();
        final Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                try {

                    System.out.println("#44  Serviço Executendo");


                    new ServicesCancelOperation(
                        ContextFrame.contextServices,
                        ContextFrame.operationRepository,
                        ContextFrame.userLoginCast,
                        ContextFrame.servicesHttp
                    );
                    if(!ContextFrame.closeOperationRun) {
                        ContextFrame.closeOperationRun = true;
                        new ServicesCloseOperation(
                                ContextFrame.contextServices,
                                ContextFrame.operationRepository,
                                ContextFrame.userLoginCast,
                                ContextFrame.servicesHttp, new OnResponseHttp() {
                                @Override
                                public void onResponse(Object data) {
                                    ContextFrame.closeOperationRun = false;
                                }

                                @Override
                                public void onError(Object error) {
                                    ContextFrame.closeOperationRun = false;
                                }
                            }
                        );
                    }

                    System.out.println("#44  Serviço Executendo 1");
                   new ServicesAcceptedOperation(
                        ContextFrame.contextServices,
                        ContextFrame.operationRepository,
                        ContextFrame.userLoginCast,
                        ContextFrame.servicesHttp
                   );
                    System.out.println("#44  Serviço Executendo 2");
                    new ServicesPointsOperation(
                        ContextFrame.contextServices,
                        ContextFrame.deliveryStateRepository,
                        ContextFrame.userLoginCast,
                        ContextFrame.servicesHttp
                    );

                    new ServicesPathTraveled(
                        ContextFrame.contextServices,
                        ContextFrame.routerMovimentBusiness,
                        ContextFrame.userLoginCast,
                        ContextFrame.servicesHttp
                    );
                    System.out.println("#44  Serviço Executendo 3");
                    new ServicesPhotoOperation(
                        ContextFrame.contextServices,
                        ContextFrame.photosRepository,
                        ContextFrame.userLoginCast,
                        ContextFrame.servicesHttp
                    );
                    System.out.println("#44  Serviço Executendo 4");

                }catch (Exception e){
                    e.printStackTrace();
                    System.out.println("#12 ERRO Service Services  "+e.getMessage());
                }

                if(!ContextFrame.stop){
                    handler.postDelayed(this, 5*1000);
                }
            }
        });

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        ContextFrame.stop=true;
        super.onDestroy();
    }


    public void timeSom(int i) {
        try {
            ContextFrame.playlerRouter.statePlayer();
            Thread.sleep(i);
        } catch (Exception e) {
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private static class ContextFrame {
        public Playler playlerRouter;
        public Context contextServices;
        public AppOpeationsRepository operationRepository;
        public Boolean stop=false;
        public ServerConfiguration conf;
        public ConfigurationsUser configurationsAdapter;
        public ServicesHttp servicesHttp;
        public UserLoginCast userLoginCast;
        public PhotosPartnersModel photosRepository;
        public DeliveryStateReaderDbHelper deliveryStateRepository;
        public RouterMovimentBusiness routerMovimentBusiness;
        public boolean closeOperationRun = false;
    }
}
