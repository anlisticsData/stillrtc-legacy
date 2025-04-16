package br.com.stilldistribuidora.pco.servicos;

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
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.LocationCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import br.com.stilldistribuidora.pco.api.ApiPco;
import br.com.stilldistribuidora.pco.config.Constante;
import br.com.stilldistribuidora.pco.db.dao.DevicesStatus;
import br.com.stilldistribuidora.pco.db.dao.OperationsModelAcess;
import br.com.stilldistribuidora.pco.db.dao.PictureDao;
import br.com.stilldistribuidora.pco.db.dao.TblSincronizarDao;
import br.com.stilldistribuidora.pco.db.dao.wsConfig;
import br.com.stilldistribuidora.pco.db.model.DevicesModel;
import br.com.stilldistribuidora.pco.db.model.Movimentos;
import br.com.stilldistribuidora.pco.db.model.PictureImageGrafica;
import br.com.stilldistribuidora.pco.db.model.TblSincronizar;
import br.com.stilldistribuidora.pco.db.model.WsConfig;
import br.com.stilldistribuidora.pco.ui.movimentos_list_grf;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.db.models.Prefs;
import br.com.stilldistribuidora.stillrtc.utils.Utils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServiceAppGrafica extends Service {
    private final OperationsModelAcess operationsModelAcess;
    private String USER_ATIVO = "";
    public Boolean ativo = true,is_upload_ativo=false;
    public static final String TAG = ServiceAppGrafica.class.getSimpleName();
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 5f;
    private static  int TIME_DEFAULT = 20*1000;
    public HandlerThread handlerThread, handlerThreadCronometro, handlerThreadGps, handlerThreadGpsUpload;
    public Handler handler, handlerTime, handlerGps, handlerGpsUpload;
    private Prefs prefs;
    private Context context;
    private LocationCallback mLocationCallback;
    private NotificationManager manager;
    private String NOTIFICATION_CHANNEL_ID = "2019081020191057123";
    private wsConfig wsConfigCotroller;
    private PictureDao wsPictoresCotroller;
    private TblSincronizarDao sincronizarController;
    private DevicesStatus devicesStatusController;
    private int deviceStatusSendingTime = 15;
    private int deviceStatusSendingTimeUpload = 10;
    private Context self;
    private GpsPco gps;
    private String jwt_token = "";
    private String jwt_device = "";
    private HandlerThread handlerThreadSincroApi;
    private HandlerThread HandlerThreadApi;
    private TblSincronizarDao sincronizadoController;

    private boolean processing=false;


    public ServiceAppGrafica() {
        self = this;
        wsConfigCotroller = new wsConfig(self);
        wsPictoresCotroller = new PictureDao(self);
        sincronizarController = new TblSincronizarDao(self);
        operationsModelAcess = new OperationsModelAcess(self);


    }
    @Override
    public void onCreate() {
        super.onCreate();
        handlerThread = new HandlerThread("HandlerThread");
        handlerThreadCronometro = new HandlerThread("HandlerTheadCronometro");
        handlerThreadGps = new HandlerThread("HandlerThreadGps");
        handlerThreadGpsUpload = new HandlerThread("HandlerThreadGpsUpload");
        HandlerThreadApi = new HandlerThread("HandlerThreadApi");
        handlerThreadSincroApi = new HandlerThread("handlerThreadSincroApi");
        Log.e(TAG, "onCreate");
        initializeLocationManager();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        jwt_token = (String) wsConfigCotroller.getBy(new WsConfig(Constante.TOKEN_JWT));
        jwt_device = (String) wsConfigCotroller.getBy(new WsConfig(Constante.DEVICE_ACESS));
        USER_ATIVO = (String) wsConfigCotroller.getBy(new WsConfig(Constante.USER_ATIVO));
        if (!handlerThread.isAlive()) {
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper());
            /*Cria Tread de Excução para sicronizar fotos*/
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    /*loop infinito do Service*/
                    while (true) {
                        /*verifica se tem internet disponivel*/
                        System.out.println("#F- Foto");
                       if (Utils.isNetworkAvailable(self)) {
                            List<PictureImageGrafica> arrayList = (List) wsPictoresCotroller.getAll();
                            /*verifica se tem itens para sincronizar*/
                            if (arrayList.size() > 0) {
                                for (PictureImageGrafica pic : arrayList) {
                                    if (pic.getSincronizado().trim().equals("0") && !is_upload_ativo) {
                                        is_upload_ativo = true;
                                        System.out.println("#1- Foto");
                                        syncFotos(context, pic);
                                    }
                                }
                            }
                            /*verifica se tem itens para sincronizar*/
                        }
                        /*verifica se tem internet disponivel*/
                        try {
                            Thread.sleep(TIME_DEFAULT);
                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    /*loop infinito do Service*/
                }
            };
            handler.post(runnable);
        }

        if (!handlerThreadCronometro.isAlive()) {
            handlerThreadCronometro.start();
            handlerTime = new Handler(handlerThreadCronometro.getLooper());
            Runnable temporizadores = new Runnable() {
                @Override
                public void run() {
                    //Inicia o cronometro
                    cronometro();
                }
            };
            handlerTime.post(temporizadores);
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


        //Faz o Upload do Pings
        if (!handlerThreadGpsUpload.isAlive()) {
            handlerThreadGpsUpload.start();
            handlerGpsUpload = new Handler(handlerThreadGpsUpload.getLooper());
            Runnable GpsUploadPoints = new Runnable() {
                @Override
                public void run() {
                    uploadPoitsDevices();
                }
            };
            handlerGpsUpload.post(GpsUploadPoints);
        }
        //Faz o Upload dos Pings

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            String channelName = "Grafica Services..";
            NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);
            Intent n = new Intent(self, movimentos_list_grf.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(self, 0, n, 0);
            Notification notification = new NotificationCompat.Builder(self, NOTIFICATION_CHANNEL_ID)
                    .setContentTitle("Grafica-Services")
                    .setContentText("Ativo")
                    .setSmallIcon(R.drawable.ic_cast_dark)
                    .setContentIntent(pendingIntent)
                    .build();
            startForeground(1000, notification);
        }


        //Sincronização com api
        //handlerThreadSincroApi

        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    try{
                       //operationsModelAcess
                       nonSynchronizedOperations("N");
                       // Thread.sleep(5*1000);
                       TimeUnit.SECONDS.sleep(2);
                    }catch (Exception e){}
                }
            }
        }).start();

        /*Cria Tread de Excução para sicronizar fotos*/
        return ServiceAppGrafica.START_NOT_STICKY;
    }

    private void nonSynchronizedOperations(String status) {

        List<Movimentos> operationsArray = (List<Movimentos>) operationsModelAcess.nonSynchronizedOperations(status);
        System.out.println("#3 Lista da Sincronizar :"+String.valueOf(operationsArray.size()));
        System.out.println("#3 Sinal da Internet    :"+Utils.isNetworkAvailable(self));
        System.out.println("#3 Processando    :"+processing);



        if(operationsArray.size() > 0) {

             for(Movimentos op:operationsArray){
                 System.out.println("#4 "+op.getMv_startus());
                 if(op.getMv_startus().equals("E")){
                     operationsModelAcess.operationsUpdateNotSynchronized(op.getMv_codigo());
                     operationDelivered(op);
                 }else if(op.getMv_startus().equals("T")){
                     if(op.getMv_sincronizado()==null){
                         operationsModelAcess.operationsUpdateNotSynchronized(op.getMv_codigo());
                         operationStart(op);
                     }else{
                         setStatusOperations(jwt_token, USER_ATIVO, op.getMv_codigo(), "T");
                     }
                 }else if(op.getMv_startus().equals("P")) {
                    setStatusOperations(jwt_token, USER_ATIVO, op.getMv_codigo(), "P");
                 }

             }
        }

    }

    private void operationStart(final Movimentos op) {
            if (!Utils.isNetworkAvailable(self)) {
                 return ;
            }

            String Autorizacao="1";

            new Thread(new Runnable() {
                @Override
                public void run() {
                    processing=true;
                    String[] tmpUser = USER_ATIVO.trim().replace( "|", ";" ).split( ";" );
                    new ApiPco().start_retiradas_grafica().start_retiradas_grafica( jwt_token,jwt_device,
                            op.getMv_codigo(),op.getMv_qt_retirada(),tmpUser[0],"1",  "" ).enqueue( new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (response.isSuccessful()) {
                                JSONArray array_data = null;
                                JSONObject dados;
                                try {
                                    JSONObject jsonObject = new JSONObject( response.body().string() );
                                    String error = jsonObject.getString( "error" );
                                    String http = jsonObject.getString( "http" );
                                    if (error.equals( "false" ) && http.equals( "200" )) {
                                        Date d = new Date();
                                        String data = jsonObject.getString( "data" );
                                        array_data = new JSONArray( data );
                                        dados = new JSONObject( array_data.get( 0 ).toString() );

                                    }

                                    setStatusOperations(jwt_token, USER_ATIVO, op.getMv_codigo(), "T");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }finally {

                                }
                            }else{
                                try {
                                    JSONObject jObjError = new JSONObject(response.errorBody().string());

                                    System.out.println("#-1 Erros: "+jObjError.getJSONObject("error").getString("message"));

                                } catch (Exception e) {
                                    //Toast.makeText(self, e.getMessage(), Toast.LENGTH_LONG).show();
                                    System.out.println("#-1 Erros: "+e.getMessage());
                                }
                            }

                            processing=false;
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            System.out.println("#3  retrofi 3");
                            processing=false;
                        }
                    } );

                }
            }).start();

   }

    private void setStatusOperations(final String jwt_token, final String user_ativo, final String  mv_codigo, final String status) {

        if (!Utils.isNetworkAvailable(self)) {

            return ;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                processing=true;
                new ApiPco().api().ws_gf_status_operation_set(jwt_token, user_ativo, mv_codigo, status)
                        .enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                if (response.isSuccessful()) {

                                    try {
                                        JSONObject jsonObject = new JSONObject(response.body().string());
                                        String error = jsonObject.getString("error");
                                        String http = jsonObject.getString("http");
                                        if (error.equals("false") && http.equals("200")) {

                                            JSONArray data = jsonObject.getJSONArray("data");
                                            JSONObject out = new JSONObject(data.get(0).toString());
                                            System.out.println("#4 "+String.format("%s | %s ",mv_codigo,status));
                                            String isUpdate=out.getString("msn");
                                            if(isUpdate.equals("1")) {
                                                operationsModelAcess.operationsUpdateSynchronized(mv_codigo);
                                            }

                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }else{
                                    try {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                                     //   Toast.makeText(self, jObjError.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();

                                        System.out.println("#-1 Erros: "+jObjError.getJSONObject("error").getString("message"));
                                    } catch (Exception e) {
                                       // Toast.makeText(self, e.getMessage(), Toast.LENGTH_LONG).show();
                                        System.out.println("#-1 Erros: "+e.getMessage());
                                    }
                                }

                                processing=false;
                            }

                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                System.out.println("#3 Erro retro 1");
                                processing=false;
                            }
                        });

            }
        }).start();



    }

    private void operationDelivered(final Movimentos op) {
        if (!Utils.isNetworkAvailable(self)) {
            return ;
        }

        final String[] tmpUser = USER_ATIVO.trim().replace( "|", ";" ).split( ";" );

        new ApiPco().save_retiradas_grafica_finish().save_retiradas_grafica_finish(jwt_token, jwt_device,
                op.getMv_qt_retirada(),op.getMv_codigo(), tmpUser[0],op.getMv_dt_retirada()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try{


                        String result = response.body().string();

                        System.out.println("#6" +
                                "  -> "+result+" op -> "+op.getMv_codigo());


                        JSONObject jsonObject = new JSONObject(result);
                        String error = jsonObject.getString("error");
                        String http = jsonObject.getString("http");
                        if (error.equals("false") && http.equals("200")) {


                            TblSincronizar sincronizar = new TblSincronizar();
                            sincronizar.setCodigo_mv(op.getMv_codigo());
                            sincronizar.setDt_create((String) sincronizarController.getByTime(sincronizar));
                            sincronizar.setStatus("T");
                            if(sincronizarController.update(sincronizar) >0 ) {
                                setStatusOperations(jwt_token, USER_ATIVO, op.getMv_codigo(), "E");
                                processing=false;
                            }

                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else{
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                       // Toast.makeText(self, jObjError.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                        System.out.println("#-1 Erros: "+jObjError.getJSONObject("error").getString("message"));

                    } catch (Exception e) {
                       // Toast.makeText(self, e.getMessage(), Toast.LENGTH_LONG).show();
                        System.out.println("#-1 Erros: "+e.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("#3  retrofit 2");
                processing=false;
            }
        });





    }

    private void uploadPoitsDevices() {


        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {

                    try {

                        devicesStatusController = new DevicesStatus(self);
                        List<String> deviceStatus = devicesStatusController.getDevicesStatus("A");

                        for (String status : deviceStatus) {

                            final String[] split = status.split("\\|");
                            //   opcao =key_+"|"+codigo+"|"+lat_lon+"|"+device+"|"+user;
                            new ApiPco().api().savePartnerPosition(jwt_token, split[1], split[3], split[2], split[4]).
                                    enqueue(new Callback<ResponseBody>() {
                                        @Override
                                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                            if (response.isSuccessful()) {
                                                System.out.println("###c");
                                                try {
                                                    JSONObject jsonObject = new JSONObject(response.body().string());
                                                    String error = jsonObject.getString("error");
                                                    String http = jsonObject.getString("http");
                                                    if (error.equals("false") && http.equals("200")) {
                                                        devicesStatusController = new DevicesStatus(self);
                                                        devicesStatusController.removeStatusDevices(split[0]);
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                    is_upload_ativo = false;

                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                    is_upload_ativo = false;
                                                }
                                            }


                                        }

                                        @Override
                                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                                        }
                                    });


                            System.out.println("TETSTE " + split[0]);


                        }

                        Thread.sleep(10000 * deviceStatusSendingTimeUpload);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }

            }
        }).start();



    }


    private void insertCurrentPositionGps() {
        final TblSincronizar operacaoSincronizada = new TblSincronizar();
        double latitude = 0.0;
        double longitude = 0.0;
       while (true) {

            System.out.println("##1 tempo");
            try {
                gps = new GpsPco(self);
                // verifica ele
                if (gps.canGetLocation()) {
                    // passa sua latitude e longitude para duas variaveis
                    latitude = gps.getLatitude();
                    longitude = gps.getLongitude();
                }
                operacaoSincronizada.setStatus("T");
                List<String> byTimeCronometro = (List<String>) sincronizarController.getByTimeCronometro(operacaoSincronizada);
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                Date date = null;
                for (String op : byTimeCronometro) {
                    devicesStatusController = new DevicesStatus(self);
                    String[] dados = op.split(",");
                    System.out.println("VVVV  [" + dados[0] + "]   - > " + dados[1] + " lat: " + latitude + " Lon: " + longitude);
                    date = new Date();
                    DevicesModel devicesstatus = new DevicesModel();
                    devicesstatus.setCod_mov(dados[1]);
                    devicesstatus.setLat_log(String.valueOf(latitude) + "," + String.valueOf(longitude));
                    devicesstatus.setDevice(jwt_device);
                    devicesstatus.setStatus("A");
                    devicesstatus.setCreated_at(dateFormat.format(date));
                    devicesstatus.setUser(USER_ATIVO);
                    devicesStatusController.insert(devicesstatus);
                }


                Thread.sleep(10000 * this.deviceStatusSendingTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    private void cronometro() {

        final TblSincronizar operacaoSincronizada = new TblSincronizar();
        new Thread() {

            @Override
            public void run() {
                int seconds = 0;
                int minuto = 0;
                int hora = 0;
                String tempo_new = "";
                while (true) {
                    operacaoSincronizada.setStatus("T");
                    System.out.println("#P op ->"+operacaoSincronizada.getCodigo_mv());
                    List<String> byTimeCronometro = (List<String>) sincronizarController.getByTimeCronometro(operacaoSincronizada);
                    for (String op : byTimeCronometro) {
                        try {
                            String[] dados = op.split(",");
                            String[] tempo = dados[2].split(":");
                            try {
                                seconds = Integer.parseInt(tempo[2]);
                            }catch (Exception e){
                                seconds = Integer.parseInt("00");
                            }
                            try {
                                seconds = Integer.parseInt(tempo[2]);
                            }catch (Exception e){
                                seconds = Integer.parseInt("00");
                            }

                            try {
                                minuto = Integer.parseInt(tempo[1]);
                            }catch (Exception e){
                                minuto = Integer.parseInt("00");
                            }
                            try {
                                hora = Integer.parseInt(tempo[0]);
                            }catch (Exception e){
                                hora = Integer.parseInt("00");
                            }

                            try {
                                seconds++;
                                if (seconds == 60) {
                                    if (minuto == 60) {
                                        hora++;
                                        minuto = 0;
                                    }
                                    minuto++;
                                    seconds = 0;
                                }
                                tempo_new = String.format("%02d", hora) + ":" + String.format("%02d", minuto) + ":" + String.format("%02d", seconds);
                                operacaoSincronizada.setDt_create(tempo_new);
                                operacaoSincronizada.setCodigo_mv(dados[1]);
                                System.out.println("#P  -> "+tempo_new);
                                sincronizarController.update(operacaoSincronizada);
                                updateGraphicsOperationTime(operacaoSincronizada);
                                System.out.println("hora: " + tempo_new);
                                Thread.sleep(1 * 1000);
                                // Thread.sleep( 6);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } catch (Exception e) {

                            e.printStackTrace();
                            System.out.println("ss");
                        }
                    }

                }

            }
        }.start();

    }


    private void updateGraphicsOperationTime(TblSincronizar op) {

        new ApiPco().api().updateGraphicsOperationTime(jwt_token, op.getCodigo_mv(), op.getDt_create()).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if(response.isSuccessful()){
                    System.out.println("ddd");
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });


    }

    private void syncFotos(Context context, PictureImageGrafica pic) {
        UploadFile uploadFile = new UploadFile();
        uploadFile.execute(pic);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handlerThread.quit();
        ativo = false;

    }


    int serverResponseCode = 0;

    public class UploadFile extends AsyncTask<Object, String, String> {
        String file_name = "";
        @Override
        protected String doInBackground(Object[] params) {


            final PictureImageGrafica obj = (PictureImageGrafica) params[0];
            File file = new File(obj.getPath_file());
            Bitmap bmp = BitmapFactory.decodeFile(obj.getPath_file());

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.JPEG, 25 /*ignored for PNG*/, bos);
                byte[] bitmapdata = bos.toByteArray();

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
                final MultipartBody.Part boby = MultipartBody.Part.createFormData("uploadedfile", file.getName(), reqFile);
                String dados = "token_session:" + jwt_token + "|device:" + jwt_device + "|file_name:" +
                        obj.getName_file() + "|user_action:" + obj.getUser() + "|operacao_grf:" + obj.getTb_retirada_gf_codigo() + "|lat_long:" + obj.getLoc().replace("|", ",");
                System.out.println("2### " + dados);
                final MultipartBody.Part name =
                        MultipartBody.Part.createFormData("name", dados);
                PictureImageGrafica tmp = (PictureImageGrafica) wsPictoresCotroller.getById(Constante.WS_PCO_GRF_MV_FT_CODIGO + "=" + obj.getCodigo());
                if (tmp.getSincronizado().trim().equals("0")) {
                    //processa a foto

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            new ApiPco().api().save_retiradas_grafica_fotos_app(name, boby).enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                    if (response.isSuccessful()) {
                                        System.out.println("###c");
                                        try {
                                            JSONObject jsonObject = new JSONObject(response.body().string());
                                            String error = jsonObject.getString("error");
                                            String http = jsonObject.getString("http");
                                            if (error.equals("false") && http.equals("200")) {
                                                PictureImageGrafica tmp = (PictureImageGrafica) wsPictoresCotroller.getById(Constante.WS_PCO_GRF_MV_FT_CODIGO + "=" + obj.getCodigo());
                                                if (tmp.getSincronizado().trim().equals("0") && is_upload_ativo) {
                                                    obj.setSincronizado("1");
                                                    wsPictoresCotroller.update(obj);
                                                    System.out.println("#1- Foto Salva");
                                                    is_upload_ativo = false;
                                                }
                                            }

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                            is_upload_ativo = false;

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            is_upload_ativo = false;
                                        }
                                    }


                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    is_upload_ativo = false;
                                }
                            });

                        }
                    }).start();

                    //processa a foto
                }





            return file_name;
        }

    }


}