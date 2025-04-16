package br.com.stilldistribuidora.stillrtc.services;

/**
 * Created by Still Technology and Development Team on 21/05/2017.
 */

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import br.com.stilldistribuidora.stillrtc.AppCompatActivityBase;
import br.com.stilldistribuidora.stillrtc.api.ApiClient;
import br.com.stilldistribuidora.stillrtc.api.ApiEndpointInterface;
import br.com.stilldistribuidora.stillrtc.db.Constants;
import br.com.stilldistribuidora.stillrtc.db.Core;
import br.com.stilldistribuidora.stillrtc.db.business.OperationBusiness;
import br.com.stilldistribuidora.stillrtc.db.business.PictureBusiness;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.ConfigDataAccess;
import br.com.stilldistribuidora.stillrtc.db.models.Config;
import br.com.stilldistribuidora.stillrtc.db.models.Operation;
import br.com.stilldistribuidora.stillrtc.db.models.Picture;
import br.com.stilldistribuidora.stillrtc.db.models.Prefs;
import br.com.stilldistribuidora.stillrtc.utils.Utils;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class ServiceApp extends Service {


    public static final String TAG = ServiceApp.class.getSimpleName();
    public HandlerThread handlerThread;
    public Handler handler;
    private Prefs prefs;

    private Context context;


    public static class ApiConfig{
        private  AppCompatActivityBase Api;
        public   ApiConfig(Context ctx){
            Api = new AppCompatActivityBase(ctx);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {




        context = ServiceApp.this;

        Log.d(TAG, "onStart");
        prefs = Core.getPrefs(context);

        if (!handlerThread.isAlive()) {
            handlerThread.start();
            handler = new Handler(handlerThread.getLooper());

            Runnable runnable = new Runnable() {
                @Override
                public void run() {



                    if(Utils.isOnline() && Utils.isNetworkAvailable(context)) {

                        PictureBusiness pictureBusiness = new PictureBusiness(context);
                        ArrayList<Picture> arrayList = (ArrayList<Picture>) pictureBusiness.getList(Constants.KEY_SYNC + "=0", "");
                        if (arrayList.size() > 0) {

                            for (Picture pic : arrayList) {
                                syncFotos(context, pic);
                            }
                        } else {
                            System.out.println("# 6 "+Utils.isOnline());
                            System.out.println("# 6"+Utils.isNetworkAvailable(context));
                            Log.wtf(TAG, "No image in slack");
                        }

                        syncOperacao(context);

                    } else {
                        Log.wtf(TAG, "[*** NO CONNECTION INTERNET ***]");
                        System.out.println("# 6   1"+Utils.isOnline());
                        System.out.println("# 6   1"+Utils.isNetworkAvailable(context));
                    }

                    handler.postDelayed(this, (1000 * prefs.getTimeLoopPictures()));
                }
            };
            handler.post(runnable);



        }
        return Service.START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public ServiceApp() {
    }

    @Override
    public void onCreate() {
        handlerThread = new HandlerThread("HandlerThread");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();




            handlerThread.quit();


    }

    public static void syncFotos(Context context, final Picture picture) {

        final PictureBusiness picBusiness = new PictureBusiness(context);
        ServiceApp.ApiConfig apiConfig = new ApiConfig(context);
        ApiEndpointInterface apiService =
                ApiClient.getClient(
                         apiConfig.Api.getEndPointAccess().getDataJson()).
                        create(ApiEndpointInterface.class);

        File file = new File(picture.getUri());

        // create RequestBody instance from file
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), reqFile);

        byte[] data = new byte[0];
        try {
            data = picture.getCreatedAt().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);

        Call<Picture.Result> call = apiService.registrarFotoDevice(
                apiConfig.Api.getDeviceUuid().getDataJson(),
                apiConfig.Api.getEndPointAccessToken().getDataJson(),
                apiConfig.Api.getEndPointAccessTokenJwt().getDataJson()
                ,base64, picture.getLatitude(), picture.getLongitude(),
                picture.getType(), picture.getDeviceId(), picture.getDeliveryId(), body);

        try {
            Picture.Result response = call.execute().body();

            if(response != null){
                if(response.ar.size()>0){

                    picture.setPicUid(response.ar.get(0).getPicId());
                    picture.setSync(1);
                    picBusiness.update(picture, Constants.ID +"="+ picture.getId());

                    Log.i(TAG, "Imagem sincronizada com sucesso...");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Erro ao enviar imagem...");
        }
    }

    public void syncOperacao(Context context){

        OperationBusiness op = new OperationBusiness(context);
        ArrayList<Operation> arrOperations = (ArrayList<Operation>) op.getList(Constants.KEY_SYNC+"=0","");

        if(arrOperations.size() > 0 ){
            for (Operation operation: arrOperations){

                Log.wtf(TAG, "Tem operacao: "+operation.getDeliveryFragmentId());
                Core.registrarFimOperacao(this, operation.getDeliveryFragmentId(), operation.getDeviceId(), operation.getDatetimeEnd());
            }
        }
    }
}