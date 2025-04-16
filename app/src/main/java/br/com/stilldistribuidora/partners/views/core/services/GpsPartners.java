package br.com.stilldistribuidora.partners.views.core.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Base64;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import br.com.stilldistribuidora.partners.views.HomeParterns;
import br.com.stilldistribuidora.partners.views.core.entity.OperationPartnerEntity;
import br.com.stilldistribuidora.partners.views.core.entity.PartnersOperationControlStateEntity;
import br.com.stilldistribuidora.partners.views.core.entity.PhotoEntity;
import br.com.stilldistribuidora.partners.views.core.lib.PartenerHelpes;
import br.com.stilldistribuidora.partners.views.core.models.OperationControlStateModel;
import br.com.stilldistribuidora.partners.views.core.models.OperationsPartnersModel;
import br.com.stilldistribuidora.partners.views.core.models.PhotosPartnersModel;
import br.com.stilldistribuidora.pco.servicos.GpsPco;
import br.com.stilldistribuidora.stillrtc.AppCompatActivityBase;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.api.ApiClient;
import br.com.stilldistribuidora.stillrtc.api.ApiEndpointInterface;
import br.com.stilldistribuidora.stillrtc.db.models.DeviceStatus;
import br.com.stilldistribuidora.stillrtc.interfac.OnClick;
import br.com.stilldistribuidora.stillrtc.utils.Utils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GpsPartners  extends Service {
    private GpsPco gps;
    private Context self;

    private NotificationManager manager;
    private Context context;
    private boolean processarServico=true;
    private   OnClick onClick;
    private HandlerThread handlerThreadGpsUpload;
    private HandlerThread handlerThreadGps;
    public Handler handler, handlerTime, handlerGps, handlerGpsUpload,handlerPhotos;
    private HandlerThread handlerThreadPhotos;
    private PhotosPartnersModel partnersModel;
    private boolean is_upload_ativo=false;
    public static  ApiConfig apiConfig;
    public static ApiEndpointInterface apiService;
    private Handler stopwatch;
    private HandlerThread instantaneousStopwatch;
    private HandlerThread handlerThreadsynchronizeOperationsStatus;
    private Handler handlerThreadsynchronizeOperationsStatusHandler;
    private OperationsPartnersModel partnersOperationModel;
    private String deviceIdentifier;
    private String deliveryFragmentId;
    private String routerId;
    private OperationControlStateModel partnersOperationControlStateModel;
    private String deviceIdentifierAuto;
    private boolean is_upload_ativo_photo=false;


    @Override
    public void onCreate() {
        super.onCreate();
        self = this;
        partnersModel = new PhotosPartnersModel(self);
        partnersOperationModel=new OperationsPartnersModel(self);
        partnersOperationControlStateModel=new OperationControlStateModel(self);
        handlerThreadGps = new HandlerThread("HandlerThreadGps");
        handlerThreadPhotos=new HandlerThread("HandleThreadUploads");
        instantaneousStopwatch=new HandlerThread("HandlerinstantaneousStopwatch");
        handlerThreadsynchronizeOperationsStatus=new HandlerThread("HandlerThreadsynchronizeOperationsStatus");
        apiConfig = new ApiConfig(context);
        apiService= ApiClient.getClient(apiConfig.Api.getEndPointAccess().getDataJson()).create(ApiEndpointInterface.class);
    }

    public  GpsPartners(){
        this.context=GpsPartners.this;
         onClick =new OnClick() {
            @Override
            public void onClick(Object obj) {
                System.out.println("#4 trhes => "+((String) obj));
            }
        };


    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle extras = intent.getExtras();
        deviceIdentifier = extras.getString("deviceIdentifier");
        deliveryFragmentId  = extras.getString("deliveryFragmentId");
        deliveryFragmentId  = extras.getString("deliveryFragmentId");
        deviceIdentifierAuto = extras.getString("deviceIdentifierAuto");
        routerId= extras.getString("routerId");
        if(!handlerThreadsynchronizeOperationsStatus.isAlive()){
            handlerThreadsynchronizeOperationsStatus.start();
            handlerThreadsynchronizeOperationsStatusHandler=new Handler(handlerThreadsynchronizeOperationsStatus.getLooper());
            Runnable synchronizeOperation=new Runnable() {
                @Override
                public void run() {
                    while (processarServico) {
                        try{
                            List<OperationPartnerEntity> operations=(List<OperationPartnerEntity>)partnersOperationModel.haveOperationsToSync("0");
                            System.out.println("#4 tempo Sincronizar Operação");
                            for(OperationPartnerEntity deviceStatus:operations){
                                System.out.println("#4 tempo Sincronizar Operação "+deviceStatus.getId());
                                if(deviceStatus.getLatitude()==null || deviceStatus.getLatitude()==null){
                                    System.out.println("#4  Error loati");
                                    deviceStatus.setSync("1");
                                    deviceStatus.setStatus("E");
                                    partnersOperationModel.operationSyncedOnServer(deviceStatus);
                                }else{
                                    System.out.println("#4  Tentando Salvar Operacao");
                                    Call<DeviceStatus.Result> call;
                                    if (Utils.isNetworkAvailable(context) && Utils.isOnline()) {
                                        call = apiService.registrarStatusDispositivo(
                                                apiConfig.Api.getDeviceUuid().getDataJson(),
                                                apiConfig.Api.getEndPointAccessToken().getDataJson(),
                                                apiConfig.Api.getEndPointAccessTokenJwt().getDataJson(),
                                                Double.parseDouble(deviceStatus.getLatitude()),
                                                Double.parseDouble(deviceStatus.getLongitude()),
                                                0,
                                                "#",
                                                deviceStatus.getDeviceId(),
                                                Integer.valueOf(deviceStatus.getDelivery_fragment_id()),
                                                deviceStatus.getCreated_at());

                                        call.enqueue(new Callback<DeviceStatus.Result>() {
                                            @Override
                                            public void onResponse(Call<DeviceStatus.Result> call, Response<DeviceStatus.Result> response) {
                                                if (response != null) {

                                                    if (response.isSuccessful()) {
                                                        deviceStatus.setSync("1");
                                                        partnersOperationModel.operationSyncedOnServer(deviceStatus);
                                                        System.out.println("#4  --sanvo");
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<DeviceStatus.Result> call, Throwable t) {
                                                System.out.println("#4  ");
                                            }
                                        });
                                    }
                                }
                            }
                            Thread.sleep(10*1000);
                        }catch (Exception e){
                            System.out.println("#4 "+e.getMessage());
                        }
                    }
                }
            };
            handlerThreadsynchronizeOperationsStatusHandler.post(synchronizeOperation);

        }
        if(!instantaneousStopwatch.isAlive()){
            instantaneousStopwatch.start();
            stopwatch=new Handler(instantaneousStopwatch.getLooper());
            Runnable stopWatchContext=new Runnable() {
                @Override
                public void run() {
                    int hora=0,minutos=0,segundos=0;

                    while (processarServico) {
                        try{
                            Object operations = partnersOperationControlStateModel.by(new String[]{deviceIdentifier, deliveryFragmentId});
                            PartnersOperationControlStateEntity operactionactive=((List<PartnersOperationControlStateEntity>) operations).get(0);
                            String[] timeCurrent = operactionactive.getTime_current().split("\\:");
                            segundos=Integer.valueOf(timeCurrent[2]);
                            minutos=Integer.valueOf(timeCurrent[1]);
                            hora=Integer.valueOf(timeCurrent[0]);
                            if(segundos < 59){
                                segundos++;
                            }else{
                                segundos=0;
                                if(minutos < 59){
                                    minutos++;
                                }else{
                                    minutos=0;
                                    hora++;
                                }

                            }
                            operactionactive.setTime_current(String.format("%02d:%02d:%02d",hora,minutos,segundos));
                            partnersOperationControlStateModel.updateTimeCurrent(operactionactive);
                            System.out.println("#4 tempo  Diplay "+    operactionactive.getTime_current());


                            Thread.sleep(10*1000);
                        }catch (Exception e){}
                    }
                }
            };
            stopwatch.post(stopWatchContext);
        }

        if (!handlerThreadGps.isAlive()) {
            handlerThreadGps.start();
            handlerGps = new Handler(handlerThreadGps.getLooper());
            Runnable Gps = new Runnable() {
                @Override
                public void run() {
                    insertCurrentPositionGps();
                }
            };
            handlerGps.post(Gps);
        }


        if (!handlerThreadPhotos.isAlive()) {
            handlerThreadPhotos.start();
            handlerPhotos = new Handler(handlerThreadPhotos.getLooper());
            Runnable PhotosSyncs = new Runnable() {
                @Override
                public void run() {
                   uploadFilesOperationsServices();
                }
            };
            handlerPhotos.post(PhotosSyncs);
        }



         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String NOTIFICATION_CHANNEL_ID = "GPS_PT_10052022";
            String channelName = "My Background Service";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);
            Intent n = new Intent(context, HomeParterns.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, n, 0);
            Notification notification = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setContentTitle("Rtc-gps")
                    .setContentText("Ativo")
                    .setSmallIcon(R.drawable.ic_baseline_explore_24_e)
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(1, notification);
        }




        return Service.START_STICKY;
    }

    private void uploadFilesOperationsServices() {
        while (processarServico) {

            try{
                if (Utils.isNetworkAvailable(self) &&   !is_upload_ativo) {
                    List<PhotoEntity> photosSyncronized =(List<PhotoEntity>)partnersModel.photosAll("0");
                    for (PhotoEntity photo : photosSyncronized){
                        if(!is_upload_ativo_photo){
                            sendPhotoToServer(photo);
                            is_upload_ativo_photo=true;

                        }

                    }

                }
                System.out.println("#4 Processando Fotos");
                Thread.sleep(9000);
            }catch (Exception e){}
         }
    }

    private void sendPhotoToServer(PhotoEntity photo) {
        UploadFile uploadFile = new UploadFile();
        uploadFile.execute(photo);


    }

    private void insertCurrentPositionGps() {
        double latitude = 0.0;
        double longitude = 0.0;
        while (processarServico) {
            System.out.println("##1 tempo");
            try {
                gps = new GpsPco(self);
                // verifica ele
                if (gps.canGetLocation()) {
                    // passa sua latitude e longitude para duas variaveis
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                    OperationPartnerEntity operationPartnerEntity = new OperationPartnerEntity();
                    operationPartnerEntity.setCreated_at(PartenerHelpes.getDate());
                    operationPartnerEntity.setLatitude(String.valueOf(latitude));
                    operationPartnerEntity.setLongitude(String.valueOf(longitude));
                    operationPartnerEntity.setId_parteners(deviceIdentifier);
                    operationPartnerEntity.setRoute_id(routerId);
                    operationPartnerEntity.setStatus("A");
                    operationPartnerEntity.setSync("0");
                    operationPartnerEntity.setDeviceId(deviceIdentifierAuto);
                    operationPartnerEntity.setDelivery_fragment_id(String.valueOf(deliveryFragmentId));
                    if(partnersOperationModel.save(operationPartnerEntity)> 0){
                        System.out.println("#4  Operação Slva");
                    }
                }
                System.out.println("#4 "+latitude+" "+longitude);
                Thread.sleep(1000 * 30);
            }catch (Exception e){}

        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        processarServico=false;
        super.onDestroy();
    }

    public static   class ApiConfig{
        private  AppCompatActivityBase Api;
        public   ApiConfig(Context ctx){
            Api = new AppCompatActivityBase(ctx);
        }
    }






    public class UploadFile extends AsyncTask<Object, String, String> {

         //12052022


        private   Bitmap resizeImage(Context context, Bitmap bmpOriginal,
                                          float newWidth, float newWeight) {
            Bitmap novoBmp = null;

            int w = bmpOriginal.getWidth();
            int h = bmpOriginal.getHeight();

            float densityFactor = context.getResources().getDisplayMetrics().density;
            float novoW = newWidth * densityFactor;
            float novoH = newWeight * densityFactor;

            //Calcula escala em percentagem do tamanho original para o novo tamanho
            float scalaW = novoW / w;
            float scalaH = novoH / h;

            // Criando uma matrix para manipulação da imagem BitMap
            Matrix matrix = new Matrix();

            // Definindo a proporção da escala para o matrix
            matrix.postScale(scalaW, scalaH);

            //criando o novo BitMap com o novo tamanho
            novoBmp = Bitmap.createBitmap(bmpOriginal, 0, 0, w, h, matrix, true);

            return novoBmp;
        }


        @Override
        protected String doInBackground(Object... objects) {
           try{
               PhotoEntity photo = (PhotoEntity) objects[0];
               PhotosPartnersModel partnersModel =new PhotosPartnersModel(context);
               File file = new File(Uri.parse(photo.getUri()).getEncodedPath());
                if(file.exists()){
                    Bitmap bitmap =BitmapFactory.decodeFile(photo.getUri());
                    //create a file to write bitmap data
                    File f = new File(context.getCacheDir(), file.getName());
                    f.createNewFile();
                    //Convert bitmap to byte array
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 25 /*ignored for PNG*/, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    //write the bytes in file
                    FileOutputStream fos = null;
                    try {
                        fos = new FileOutputStream(f);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        fos.write(bitmapdata);
                        fos.flush();
                        fos.close();


                        if (Utils.isNetworkAvailable(context) && Utils.isOnline()) {


                            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), f);
                            MultipartBody.Part body =
                                    MultipartBody.Part.createFormData("file", f.getName(), reqFile);

                            byte[] data = new byte[0];
                            data = photo.getCreated_at().getBytes("UTF-8");
                            String base64 = Base64.encodeToString(data, Base64.DEFAULT);
                            Call<ResponseBody> call = apiService.registerPicturePartners(
                                    apiConfig.Api.getDeviceUuid().getDataJson(),
                                    apiConfig.Api.getEndPointAccessToken().getDataJson(),
                                    apiConfig.Api.getEndPointAccessTokenJwt().getDataJson()
                                    ,base64,Double.parseDouble(photo.getLatitude()),Double.parseDouble(photo.getLongitude()),
                                    Integer.parseInt(photo.getType()),Integer.parseInt(photo.getDeviceId()),
                                    Integer.parseInt(photo.getDelivery_id()), body);
                            try {
                                String response = call.execute().body().string();
                                System.out.println("#4  Pro"+response);
                                try{
                                    JSONObject pictureResponse = new JSONObject(response);
                                    JSONArray pictures=pictureResponse.getJSONArray("pictures");
                                    photo.setSync("1");
                                    photo.setPath_serve_url(pictures.getJSONObject(0).getString("uri"));
                                    photo.setPic_uid(String.valueOf(pictures.getJSONObject(0).getString("id")));
                                    partnersModel.photoSyncedOnServer(photo);
                                }catch (Exception e){
                                    System.out.println("#4 "+e.getMessage());
                                }

                                is_upload_ativo_photo=false;
                            } catch (IOException e) {
                                e.printStackTrace();
                                System.out.println("#4  Erro ao enviar imagem......");
                                is_upload_ativo_photo=false;


                            }
                            is_upload_ativo_photo=false;

                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                        is_upload_ativo_photo=false;
                    }


             }
           }catch (Exception e){
               System.out.println("#4  "+e.getMessage());
               is_upload_ativo_photo=false;

           }
            return null;
        }
    }












}
