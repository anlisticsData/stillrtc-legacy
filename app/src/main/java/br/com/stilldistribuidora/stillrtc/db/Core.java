package br.com.stilldistribuidora.stillrtc.db;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.stilldistribuidora.stillrtc.AppCompatActivityBase;
import br.com.stilldistribuidora.stillrtc.api.ApiClient;
import br.com.stilldistribuidora.stillrtc.api.ApiEndpointInterface;
import br.com.stilldistribuidora.stillrtc.db.business.DeviceStatusBusiness;
import br.com.stilldistribuidora.stillrtc.db.business.OperationBusiness;
import br.com.stilldistribuidora.stillrtc.db.business.PrefsBusiness;
import br.com.stilldistribuidora.stillrtc.db.models.DeviceStatus;
import br.com.stilldistribuidora.stillrtc.db.models.Operation;
import br.com.stilldistribuidora.stillrtc.db.models.Prefs;
import br.com.stilldistribuidora.stillrtc.utils.Utils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by Ack Lay (Cleidimar Viana) on 3/11/2017.
 * E-mail: cleidimarviana@gmail.com
 * Social: https://www.linkedin.com/in/cleidimarviana/
 */

public class Core {
    private static final String TAG = Core.class.getSimpleName();

    public static void registrarDispositivoStatusWS(final Context context, final DeviceStatus deviceStatus) {
        final Core.ApiConfig appConfig = new ApiConfig(context);
        if (Utils.isNetworkAvailable(context) && Utils.isOnline()) {
            System.out.println("#5 Enviando WEB " );
            //Grarantir que seja Somente Dispositivos
            if (deviceStatus.getDeviceIdentifier().substring(0, 1).matches("[0-9]")) {
                ApiEndpointInterface apiService =
                        ApiClient.getClient(
                                appConfig.Api.getEndPointAccess().getDataJson()).
                                create(ApiEndpointInterface.class);

                Call<DeviceStatus.Result> call;
                call = apiService.registrarStatusDispositivo(
                        appConfig.Api.getDeviceUuid().getDataJson(),
                        appConfig.Api.getEndPointAccessToken().getDataJson(),
                        appConfig.Api.getEndPointAccessTokenJwt().getDataJson(),
                        deviceStatus.getLatitude(),
                        deviceStatus.getLongitude(),
                        deviceStatus.getBatery(),
                        deviceStatus.getIp(),
                        deviceStatus.getDeviceIdentifier(),
                        deviceStatus.getDeviceFragmentId(),
                        deviceStatus.getCreatedAt());

                call.enqueue(new Callback<DeviceStatus.Result>() {
                    @Override
                    public void onResponse(Call<DeviceStatus.Result> call, Response<DeviceStatus.Result> response) {

                        System.out.println("#5 RESPONSE WEB " );
                        System.out.println(response);


                        try {
                            if (response != null) {


                                if (deviceStatus.getId() > 0) {

                                    Log.wtf(TAG, "[Register] (ID: " + deviceStatus.getId()
                                            + "; \nlat: " + deviceStatus.getLatitude()
                                            + "; \nlng: " + deviceStatus.getLongitude()
                                            + "; \nbat: " + deviceStatus.getBatery()
                                            + "; \nip: " + deviceStatus.getIp()
                                            + "; \nidentifier: " + deviceStatus.getDeviceIdentifier()
                                            + "; \noperation_id: " + deviceStatus.getDeviceFragmentId()
                                            + "; \ndatetime: " + deviceStatus.getCreatedAt() + ")");


                                    DeviceStatusBusiness deviceStatusBusiness = new DeviceStatusBusiness(context);
                                    deviceStatus.setSync(1);
                                    deviceStatusBusiness.update(deviceStatus, Constants.ID + "=" + deviceStatus.getId());

                                    Log.wtf(TAG, "[Register Device Status OFF in Server] (ID: " + deviceStatus.getId()
                                            + "; \nlat: " + deviceStatus.getLatitude()
                                            + "; \nlng: " + deviceStatus.getLongitude()
                                            + "; \nbat: " + deviceStatus.getBatery()
                                            + "; \nip: " + deviceStatus.getIp()
                                            + "; \nidentifier: " + deviceStatus.getDeviceIdentifier()
                                            + "; \noperation_id: " + deviceStatus.getDeviceFragmentId()
                                            + "; \ndatetime: " + deviceStatus.getCreatedAt() + ")");


                                } else {
                                    Log.e(TAG, "[Register Device Status] ");

                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                    @Override
                    public void onFailure(Call<DeviceStatus.Result> call, Throwable t) {
                        // Log error here since request failed
                        Log.i(TAG, "[ERROR - Register Device Status]" + t.toString());
                        System.out.println("[ERROR - Register Device Status OFF in Server] (ID: " + deviceStatus.getId()
                                + "; \nlat: " + deviceStatus.getLatitude()
                                + "; \nlng: " + deviceStatus.getLongitude()
                                + "; \nbat: " + deviceStatus.getBatery()
                                + "; \nip: " + deviceStatus.getIp()
                                + "; \nidentifier: " + deviceStatus.getDeviceIdentifier()
                                + "; \noperation_id: " + deviceStatus.getDeviceFragmentId()
                                + "; \ndatetime: " + deviceStatus.getCreatedAt() + ")");


                    }
                });

                //Grarantir que seja Somente Dispositivos

            }


        }


    }

    public static void registrarFimOperacao(final Context context, final int delivery_id, int device_id, String date_start) {
        final Core.ApiConfig appConfig = new ApiConfig(context);
        ApiEndpointInterface apiService =
                ApiClient.getClient(appConfig.Api.getEndPointAccess().getDataJson()).create(ApiEndpointInterface.class);

       /* final Call<Operation.Result> call = apiService.registrarFimOperacao(
                appConfig.Api.getDeviceUuid().getDataJson(),
                appConfig.Api.getEndPointAccessToken().getDataJson(),
                appConfig.Api.getEndPointAccessTokenJwt().getDataJson(),
                delivery_id, device_id, date_start);*/

//11052022
        final Call<Operation.Result> call = apiService.registerEndOperationOf50Plus1(
                appConfig.Api.getDeviceUuid().getDataJson(),
                appConfig.Api.getEndPointAccessToken().getDataJson(),
                appConfig.Api.getEndPointAccessTokenJwt().getDataJson(),
                delivery_id, device_id, date_start);

        call.enqueue(new Callback<Operation.Result>() {
            @Override
            public void onResponse(Call<Operation.Result> call, Response<Operation.Result> response) {

                if (response != null) {
                    if (response.body() != null) {
                       // ArrayList<Operation> list = response.body().ar;
                        OperationBusiness op = new OperationBusiness(context);
                        Operation operation = (Operation) op.getById(Constants.KEY_DELIVERY_FRAGMENT_ID + "=" + delivery_id);

                        if (operation != null) {
                            operation.setSync(1);
                            op.update(operation, Constants.KEY_DELIVERY_FRAGMENT_ID + "=" + delivery_id);
                            Log.wtf(TAG, "[FINISH OPERATION](Operation terminated by synchronism.)");
                            System.out.println("# 6 [FINISH OPERATION](Operation terminated by synchronism.)");
                        }




                       /* if (list.size() > 0) {
                            OperationBusiness op = new OperationBusiness(context);
                            Operation operation = (Operation) op.getById(Constants.KEY_DELIVERY_FRAGMENT_ID + "=" + delivery_id);

                            if (operation != null) {
                                operation.setSync(1);
                                op.update(operation, Constants.KEY_DELIVERY_FRAGMENT_ID + "=" + delivery_id);
                                Log.wtf(TAG, "[FINISH OPERATION](Operation terminated by synchronism.)");
                                System.out.println("# 6 [FINISH OPERATION](Operation terminated by synchronism.)");
                            }
                        }*/
                    } else {

                        System.out.println("# 6 "+response.errorBody());

                    /*    try {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            System.out.println("# 6 "+jObjError.getString("message"));
                            Toast.makeText(context, jObjError.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        }

*/
                        Log.wtf(TAG, "[FINISH OPERATION](ERROR ao tentar sincronizar!)");
                    }

                }
            }

            @Override
            public void onFailure(Call<Operation.Result> call, Throwable t) {
                Log.wtf(TAG, "Não esta salvando as coordenadas:" + t.toString());
            }
        });
    }

    public static Prefs getPrefs(Context context) {
        final PrefsBusiness prefsBusiness = new PrefsBusiness(context);
        final List<Prefs> list = (ArrayList<Prefs>) prefsBusiness.getList("", Constants.KEY_CREATED_AT + " DESC LIMIT 1");


        return (list.size() > 0) ? list.get(0) : new Prefs();
    }

/*
    public static void registerPicture(Context context, final Picture picture) {
        AppCompatActivityBaseView appCompatActivityBaseView = new AppCompatActivityBaseView(context);
       final PictureBusiness picBusiness = new PictureBusiness(context);
        ApiEndpointInterface apiService =
                ApiClient.getClient(appCompatActivityBaseView.endPointAccess_.getDataJson()).
                        create(ApiEndpointInterface.class);

        File file = new File(picture.getUri());

        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);

        MultipartBody.Part body =
               MultipartBody.Part.createFormData("file", file.getName(), reqFile);

        Call<Picture.Result> call = apiService.
                registrarFotoDevice(appCompatActivityBaseView.endPointAccessToken_.getDataJson(),
                        appCompatActivityBaseView.endPointAccessTokenJwt_.getDataJson()
                        ,picture.getCreatedAt(), picture.getLatitude(), picture.getLongitude(), picture.getType(), picture.getDeviceId(), picture.getDeliveryId(), */
    /**//*
 body);

        call.enqueue(new Callback<Picture.Result>() {
        @Override
            public void onResponse(Call<Picture.Result> call, Response<Picture.Result> response) {

                if (response != null) {

                    if(response.body().ar.size()>0){

                       picture.setPicUid(response.body().ar.get(0).getPicId());
                       picture.setSync(1);
                       picBusiness.update(picture, Constants.ID +"="+ picture.getId());
                  }
               }
           }

            @Override
           public void onFailure(Call<Picture.Result> call, Throwable t) {
              // Log error here since request failed
              Log.e(TAG, t.toString());
               //gps.stopUsingGPS();
            }
    });
    }
*/

    public static class ApiConfig {
        private final AppCompatActivityBase Api;

        public ApiConfig(Context ctx) {
            Api = new AppCompatActivityBase(ctx);
        }
    }
}