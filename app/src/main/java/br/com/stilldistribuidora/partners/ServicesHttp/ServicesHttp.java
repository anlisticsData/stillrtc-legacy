package br.com.stilldistribuidora.partners.ServicesHttp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import br.com.stilldistribuidora.partners.Base.navegador.DeliveryStateModel;
import br.com.stilldistribuidora.partners.Base.routerMovimentDomain.RouterMovimentModel;
import br.com.stilldistribuidora.partners.Casts.ApiError;
import br.com.stilldistribuidora.partners.Casts.AvailableOperationsCast;
import br.com.stilldistribuidora.partners.Casts.CabinAudio;
import br.com.stilldistribuidora.partners.Casts.CabinAudioUpload;
import br.com.stilldistribuidora.partners.Casts.PrizesCast;
import br.com.stilldistribuidora.partners.Casts.ResponseApiGenerics;
import br.com.stilldistribuidora.partners.Casts.ResponseBaseCast;
import br.com.stilldistribuidora.partners.Casts.ResponseBaseCastData;
import br.com.stilldistribuidora.partners.Casts.ResponseBaseCastStatusDataError;
import br.com.stilldistribuidora.partners.Casts.ResponsePhoto;
import br.com.stilldistribuidora.partners.Casts.SavesTheInformationAndBringsIfThereAreNewOperationsAvailable;
import br.com.stilldistribuidora.partners.Casts.UserLoginCast;
import br.com.stilldistribuidora.partners.Contracts.OnResponseFile;
import br.com.stilldistribuidora.partners.Contracts.OnResponseHttp;
import br.com.stilldistribuidora.partners.Contracts.ServicesAbstractHttp;
import br.com.stilldistribuidora.partners.Contracts.UploadFile;
import br.com.stilldistribuidora.partners.Models.Audio;
import br.com.stilldistribuidora.partners.Models.AvailableOperationsComposition;
import br.com.stilldistribuidora.partners.Repository.RepositoryConcret.AppOpeationsRepository;
import br.com.stilldistribuidora.partners.Repository.RepositoryModels.OperationModelRepository;
import br.com.stilldistribuidora.partners.resources.CommonFunctions;
import br.com.stilldistribuidora.partners.resources.ServerConfiguration;
import br.com.stilldistribuidora.partners.resources.Resources;
import br.com.stilldistribuidora.partners.resources.WriteFilesSd;
import br.com.stilldistribuidora.partners.views.core.entity.PhotoEntity;
import br.com.stilldistribuidora.stillrtc.utils.Utils;
import br.com.stilldistribuidora.partners.Casts.DeviceCode;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ServicesHttp extends ServicesAbstractHttp {
    private final ServerConfiguration serverConfiguration;
    private final OnResponseHttp HttpGlobal;
    private final Context context;
    private OnResponseFile onResponseFile;


    public ServicesHttp(Context context, ServerConfiguration conf, OnResponseHttp onResponseHttp) {
        this.HttpGlobal = onResponseHttp;
        this.serverConfiguration = conf;
        this.context = context;
        handleError();
    }



    public void issueOperationAlertAvailable(int parternId, int operatingRangerPerDays, OnResponseHttp onResponseHttp) {
        try {

            Call<ResponseBody> issueOperationAlertAvailable = this.serverConfiguration.getAPI().issueOperationAlertAvailable(
                    this.serverConfiguration.getToken(),
                    String.valueOf(parternId), String.valueOf(operatingRangerPerDays));


            issueOperationAlertAvailable.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.body() == null) {
                            try {
                                ResponseBaseCastStatusDataError responseBaseCast = new Gson().fromJson(response.errorBody().charStream(), ResponseBaseCastStatusDataError.class);
                                onResponseHttp.onError(responseBaseCast.data);
                            } catch (Exception e) {
                                ApiError responseBaseCast = new Gson().fromJson(response.errorBody().charStream(), ApiError.class);
                                onResponseHttp.onError(responseBaseCast.getMessage());
                            }
                            return;
                        }
                        ResponseBaseCastStatusDataError responseBaseCast = new Gson().fromJson(response.body().string(), ResponseBaseCastStatusDataError.class);

                        onResponseHttp.onResponse(responseBaseCast.data);
                    } catch (Exception e) {
                        ApiError responseBaseCast = new Gson().fromJson(response.errorBody().charStream(), ApiError.class);
                        onResponseHttp.onError(responseBaseCast);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    onResponseHttp.onError(t.getMessage());
                }
            });


        } catch (Exception e) {
            onResponseHttp.onError(e.getMessage());
        }
    }





    public void searchforcabinaudios(String deliveryCode, OnResponseHttp onResponseHttp) {
        try {


            Call<ResponseBody> searchforcabinaudio = this.serverConfiguration.getAPI().searchforcabinaudio(
                    this.serverConfiguration.getToken(), deliveryCode);

            searchforcabinaudio.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                          try{
                              CabinAudioUpload responseBaseCast=null;
                              if(response.isSuccessful()){
                                   responseBaseCast = new Gson().fromJson(response.body().string(), CabinAudioUpload.class);
                                   onResponseHttp.onResponse(responseBaseCast);
                              }else{
                                  onResponseHttp.onResponse(null);
                              }



                          }catch (Exception e){
                              e.printStackTrace();
                              onResponseHttp.onResponse(null);

                          }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            onResponseHttp.onResponse(null);
                        }
            });


        } catch (Exception e) {
            onResponseHttp.onError(e.getMessage());
        }
    }



    public void registerPicturePartners(UserLoginCast user, PhotoEntity photo, byte[] data, MultipartBody.Part body, String base64, OnResponseHttp onResponseHttp) {
        try {
            Call<ResponseBody> call = this.serverConfiguration.getAPI().registerPicturePartners(
                    photo.getDeviceId(),
                    this.serverConfiguration.getToken(),
                    this.serverConfiguration.getJwt()
                    , base64, Double.parseDouble(photo.getLatitude()), Double.parseDouble(photo.getLongitude()),
                    Integer.parseInt(photo.getType()), Integer.parseInt(photo.getDeviceId()),
                    Integer.parseInt(photo.getDelivery_id()), body);
            try {
                String response = call.execute().body().string();
                ResponsePhoto responsePhoto = new Gson().fromJson(response, ResponsePhoto.class);
                ResponseApiGenerics responseApiGenerics = new ResponseApiGenerics();
                if(responsePhoto.error.equals("false")){
                    responsePhoto.pictures.get(0).id=String.valueOf(photo.getId());
                    responseApiGenerics.error=responsePhoto.error;
                    responseApiGenerics.message=responsePhoto.message;
                    responseApiGenerics.data=responsePhoto.pictures;
                    responseApiGenerics.status="200";
                    onResponseHttp.onResponse(responseApiGenerics);
                }else{
                    responseApiGenerics.error=responsePhoto.error;
                    responseApiGenerics.message=responsePhoto.message;
                    responseApiGenerics.data=responsePhoto.pictures;
                    responseApiGenerics.status="500";
                    onResponseHttp.onError(responseApiGenerics);
                }

            } catch (Exception e) {
                ApiError responseBaseCast = new ApiError();
                responseBaseCast.setMessage("Erro ao Atualizar Verifique os Dados. e Tente Novamente.!");
                onResponseHttp.onError(responseBaseCast);

            }
        } catch (Exception e) {
            ApiError responseBaseCast = new ApiError();
            responseBaseCast.setMessage("Erro ao Atualizar Verifique os Dados. e Tente Novamente.!");
            onResponseHttp.onError(responseBaseCast);
        }
    }



    public void requestDeviceCode(UserLoginCast user, OperationModelRepository delivery, OnResponseHttp onResponseHttp) {
        try{

            Call<ResponseBody>  response = this.serverConfiguration.getAPI().requestDeviceCode(
                    this.serverConfiguration.getToken(),
                    String.valueOf(delivery.getOperationID()),
                    String.valueOf(delivery.getParternID()));

            response.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try{
                        DeviceCode deviceCode = new Gson().fromJson(response.body().string(), DeviceCode.class);
                        onResponseHttp.onResponse(deviceCode);
                     }catch (Exception e){
                        ApiError responseBaseCast = new Gson().fromJson(response.errorBody().charStream(), ApiError.class);
                        onResponseHttp.onError(responseBaseCast.getMessage());
                    }
                }
                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    ApiError responseBaseCast = new Gson().fromJson("Erro#", ApiError.class);
                    onResponseHttp.onError(responseBaseCast.getMessage());
                }
            });


        }catch (Exception e){
            onResponseHttp.onError(e.getMessage());
        }
    }


    public void updateOperationRouterPointsPartners(UserLoginCast user, List<RouterMovimentModel> deliveryTraveled, OnResponseHttp onResponseHttp) {
        StringBuilder routerMap =  new StringBuilder();
        int rows=0;
        String formatJson="{\"id\":%s,\"partner\":%s,\"point\":%s,\"deviceId\":\"%s\" ,\"deliveryId\":\"%s\" ,\"action\":\"%s\" ,\"created\":\"%s\" }";

        for( RouterMovimentModel m :  deliveryTraveled){
            routerMap.append(String.format(formatJson,
                    m.getIdKeys(),user.getUuid(),m.getStartPoints(),m.getDeviceID(),m.getDeliveryId(),m.getAction(),m.getCreate_at()));
            rows++;
            if(rows < deliveryTraveled.size())
                routerMap.append(",");

       }
        System.out.println("#12 - data ["+routerMap.toString()+"]");


        Call<ResponseBody> response = this.serverConfiguration.getAPI().registerActivityOnMapByUser(
                this.serverConfiguration.getToken(),String.format("[%s]",routerMap.toString()));
        response.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    if(response.body() !=null){
                        try{
                            onResponseHttp.onResponse(response.body().string());
                            System.out.println("#12 -service points OK");
                            return;
                        }catch (Exception e){e.printStackTrace();}
                    }
                }
                onResponseHttp.onResponse(null);

                System.out.println("#12 -service points OK not Save");
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                System.out.println("#12 -service points Error "+t.getMessage());
                onResponseHttp.onResponse("Erro ao processar");
            }
        });



    }



    public void updateOperationRouterPoints(UserLoginCast user, DeliveryStateModel delivery, OnResponseHttp onResponseHttp) {
        try{

            System.out.println("#56 - Registrando Pontos");
            double latitude,longitude;
            Date dataHoraAtual = new Date();
            String data = new SimpleDateFormat("dd/MM/yyyy").format(dataHoraAtual);
            String hora = new SimpleDateFormat("HH:mm:ss").format(dataHoraAtual);
            String dataCreated = String.format("%s %s",data,hora);
            String[] latLng = delivery.getLatlng().split("\\,");
            latitude=Double.valueOf(latLng[0]);
            longitude=Double.valueOf(latLng[1]);
            Call<ResponseBody> response = this.serverConfiguration.getAPI().registrarStatusDispositivoPartner(
                    String.valueOf(delivery.getDeviceid()),
                    this.serverConfiguration.getToken(), this.serverConfiguration.getJwt(),
                    latitude, longitude, Float.valueOf(delivery.getBatery()),
                    "1", String.valueOf(delivery.getDeviceid()),
                    Integer.valueOf(delivery.getDeliveryid()), dataCreated,String.valueOf(delivery.getLaps()));

            System.out.println("#56 - Registrando Pontos 1");


            response.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {


                        if (response.body() == null) {
                            System.out.println("#56 - Registrando Pontos 2");
                            try {
                                    try {
                                        ResponseBaseCastData responseBaseCast = new Gson().fromJson(response.errorBody().charStream(),
                                                ResponseBaseCastData.class);
                                        onResponseHttp.onError(responseBaseCast);
                                    } catch (Exception e) {
                                        ApiError responseBaseCast = new ApiError();
                                        responseBaseCast.setMessage("Erro ao Atualizar Verifique os Dados. e Tente Novamente.!");
                                        onResponseHttp.onError(responseBaseCast);
                                    }
                            } catch (Exception e) {
                                System.out.println("#56 - Registrando Pontos 3");
                                ApiError responseBaseCast = new Gson().fromJson(response.errorBody().charStream(), ApiError.class);
                                onResponseHttp.onError(responseBaseCast.getMessage());
                            }
                            return;
                        }

                            if(response.isSuccessful()){
                                ResponseBaseCastData responseBaseCast = new Gson().fromJson(response.body().string(),
                                        ResponseBaseCastData.class);
                                responseBaseCast.data=delivery;
                                onResponseHttp.onResponse(responseBaseCast);
                            }
                    } catch (Exception e) {
                        //System.out.println("#56 - Registrando Pontos 4");
                        //ApiError responseBaseCast = new Gson().fromJson(response.errorBody().charStream(), ApiError.class);
                       // onResponseHttp.onError(responseBaseCast.getMessage());
                        onResponseHttp.onError("# Error");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                    System.out.println("#56 - Registrando Pontos 5");

                }
            });




        } catch (Exception e) {
            System.out.println("#56 - Registrando Pontos  6" );
            onResponseHttp.onError(e.getMessage());

        }
    }





    public void registerAccepted(UserLoginCast user, OperationModelRepository op, OnResponseHttp onResponseHttp) {

        try {


            Call<ResponseBody> response = this.serverConfiguration.getAPI().registerAccepted(
                    this.serverConfiguration.getToken(),
                    String.valueOf(op.getOperationID()),user.getUuid(),String.valueOf(op.getRouterID()));

            response.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.body() == null) {
                            try {
                                try {
                                    ResponseBaseCast responseBaseCast = new Gson().fromJson(response.errorBody().charStream(), ResponseBaseCast.class);
                                    onResponseHttp.onError(responseBaseCast.msn);

                                } catch (Exception e) {

                                    ApiError responseBaseCast = new ApiError();
                                    responseBaseCast.setMessage("Erro ao Atualizar Verifique os Dados. e Tente Novamente.!");
                                    onResponseHttp.onError(responseBaseCast);
                                }

                            } catch (Exception e) {
                                ApiError responseBaseCast = new Gson().fromJson(response.errorBody().charStream(), ApiError.class);
                                onResponseHttp.onError(responseBaseCast.getMessage());
                            }
                            return;
                        }

                        onResponseHttp.onResponse(response.body().string());

                    } catch (Exception e) {
                        ApiError responseBaseCast = new Gson().fromJson(response.errorBody().charStream(), ApiError.class);
                        onResponseHttp.onError(responseBaseCast.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    onResponseHttp.onError(t.getMessage());
                }
            });
        } catch (Exception e) {
            onResponseHttp.onError(e.getMessage());
        }
    }


    public void cancelOperation(UserLoginCast user, OperationModelRepository op, OnResponseHttp onResponseHttp) {

        try {


            Call<ResponseBody> response = this.serverConfiguration.getAPI().cancelPartnerOperation(
                    this.serverConfiguration.getToken(),
                    user.getUuid(), String.valueOf(op.getOperationID()), op.getJustification());

            response.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.body() == null) {
                            try {
                                try {
                                    ResponseBaseCast responseBaseCast = new Gson().fromJson(response.errorBody().charStream(), ResponseBaseCast.class);
                                    onResponseHttp.onError(responseBaseCast.msn);

                                } catch (Exception e) {

                                    ApiError responseBaseCast = new ApiError();
                                    responseBaseCast.setMessage("Erro ao Atualizar Verifique os Dados. e Tente Novamente.!");
                                    onResponseHttp.onError(responseBaseCast);
                                }

                            } catch (Exception e) {
                                ApiError responseBaseCast = new Gson().fromJson(response.errorBody().charStream(), ApiError.class);
                                onResponseHttp.onError(responseBaseCast.getMessage());
                            }
                            return;
                        }

                        onResponseHttp.onResponse(response.body().string());

                    } catch (Exception e) {
                        ApiError responseBaseCast = new Gson().fromJson(response.errorBody().charStream(), ApiError.class);
                        onResponseHttp.onError(responseBaseCast.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    onResponseHttp.onError(t.getMessage());
                }
            });
        } catch (Exception e) {
            onResponseHttp.onError(e.getMessage());
        }
    }

    public void operationsNearAndFarBy(UserLoginCast user, String date, OnResponseHttp onResponseHttp) {
        try {

            System.out.println("Tokem: " + serverConfiguration.getToken() + " Add : " + user.getAddress().getAddress() + " Dat " + date);



            Call<ResponseBody> response = serverConfiguration.getAPI().operationsNearAndFarBy(
                    serverConfiguration.getToken(), user.getAddress().getAddress(), "1", user.getUuid(), date,"");

            response.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.body() == null) {
                            try {
                                Reader error = response.errorBody().charStream();
                                try {
                                    ResponseBaseCast responseBaseCast = new Gson().fromJson(error, ResponseBaseCast.class);
                                    onResponseHttp.onError(responseBaseCast.msn);
                                } catch (Exception e) {
                                    ApiError responseBaseCast = new ApiError();
                                    responseBaseCast.setMessage("Erro ao Atualizar Verifique os Dados. e Tente Novamente.!");
                                    onResponseHttp.onError(responseBaseCast);
                                }
                            } catch (Exception e) {
                                ApiError responseBaseCast = new Gson().fromJson(response.errorBody().charStream(), ApiError.class);
                                onResponseHttp.onError(responseBaseCast.getMessage());
                            }
                            return;
                        }
                        String body = response.body().string();
                        AvailableOperationsCast responseBaseCast = new Gson().fromJson(body, AvailableOperationsCast.class);
                        onResponseHttp.onResponse(responseBaseCast);

                    } catch (Exception e) {
                        ApiError responseBaseCast = new Gson().fromJson(response.errorBody().charStream(), ApiError.class);
                        onResponseHttp.onError(responseBaseCast.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    onResponseHttp.onError(t.getMessage());
                }
            });
        } catch (Exception e) {
            onResponseHttp.onError(e.getMessage());
        }
    }


    public void operationsNearAndFarBy(UserLoginCast user, String date, List<String> loadedOperations , OnResponseHttp onResponseHttp) {
        try {

            System.out.println("Tokem: " + serverConfiguration.getToken() + " Add : " + user.getAddress().getAddress() + " Dat " + date);
            String loadedOperationsLocal="";
            if(loadedOperations.size()>0){
                loadedOperationsLocal=String.join("|",loadedOperations);

            }
            String address=(!user.getAddress().getAddress().isEmpty()) ? user.getAddress().getAddress() :".";
            String token = serverConfiguration.getToken();
            String type="1";
            String userId=user.getUuid();

            Call<ResponseBody> response = serverConfiguration.getAPI().
                        operationsNearAndFarBy(token,address , type, userId, date,loadedOperationsLocal);

            response.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.body() == null || !response.isSuccessful() ) {
                            try {
                                Reader error = response.errorBody().charStream();
                                try {
                                    ResponseBaseCast responseBaseCast = new Gson().fromJson(error, ResponseBaseCast.class);
                                    onResponseHttp.onError(responseBaseCast.msn);
                                } catch (Exception e) {
                                    ApiError responseBaseCast = new ApiError();
                                    responseBaseCast.setMessage("Erro ao Atualizar Verifique os Dados. e Tente Novamente.!");
                                    onResponseHttp.onError(responseBaseCast);
                                }
                            } catch (Exception e) {
                                ApiError responseBaseCast = new Gson().fromJson(response.errorBody().charStream(), ApiError.class);
                                onResponseHttp.onError(responseBaseCast.getMessage());
                            }
                            return;
                        }
                        String body = response.body().string();
                        AvailableOperationsCast responseBaseCast = new Gson().fromJson(body, AvailableOperationsCast.class);
                        onResponseHttp.onResponse(responseBaseCast);

                    } catch (Exception e) {
                        ApiError responseBaseCast = new Gson().fromJson(response.errorBody().charStream(), ApiError.class);
                        onResponseHttp.onError(responseBaseCast.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    onResponseHttp.onError(t.getMessage());
                }
            });
        } catch (Exception e) {
            onResponseHttp.onError(e.getMessage());
        }
    }




    public void updatePartnerInformations(UserLoginCast user, OnResponseHttp onResponseHttp) {
        try {
            //fazer as Validaçoes
            Call<ResponseBody> partnerInformationsCall = this.serverConfiguration.getAPI().updatePartnerInformations(
                    this.serverConfiguration.getToken(), user.getUuid(), user.getName(), user.getLastname(),
                    user.getMobilephone(), user.getEmail(), user.getAddress().getAddress(), user.getConta().getType(),
                    user.getConta().getName_completed(), user.getConta().getCpf(), user.getConta().getName_bank(),
                    user.getConta().getAgency(), user.getConta().getConta(), user.getConta().getType_pix(),
                    user.getConta().getPix()

            );

            partnerInformationsCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.body() == null) {
                            try {
                                Reader dsad = response.errorBody().charStream();
                                try {

                                    ResponseBaseCast responseBaseCast = new Gson().fromJson(dsad, ResponseBaseCast.class);
                                    onResponseHttp.onError(responseBaseCast.msn);
                                } catch (Exception e) {
                                    ApiError responseBaseCast = new ApiError();
                                    responseBaseCast.setMessage("Erro ao Atualizar Verifique os Dados. e Tente Novamente.!");
                                    onResponseHttp.onError(responseBaseCast);
                                }

                            } catch (Exception e) {
                                ApiError responseBaseCast = new Gson().fromJson(response.errorBody().charStream(), ApiError.class);
                                onResponseHttp.onError(responseBaseCast.getMessage());
                            }
                            return;
                        }
                        String body = response.body().string();
                        ResponseBaseCast responseBaseCast = new Gson().fromJson(body, ResponseBaseCast.class);
                        onResponseHttp.onResponse(responseBaseCast);
                        System.out.println("d");
                    } catch (Exception e) {
                        ApiError responseBaseCast = new Gson().fromJson(response.errorBody().charStream(), ApiError.class);
                        onResponseHttp.onError(responseBaseCast);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    onResponseHttp.onError(t.getMessage());
                }
            });


        } catch (Exception e) {
            onResponseHttp.onError(e.getMessage());
        }
    }


    public void withdrawPrize(int parternId, double stillCoinsUser, String prizesSeparatedPipe, OnResponseHttp onResponseHttp) {
        try {

            Call<ResponseBody> withdrawPrizeCall = this.serverConfiguration.getAPI().withdrawPrize(
                    this.serverConfiguration.getToken(),
                    String.valueOf(parternId), String.valueOf(stillCoinsUser), prizesSeparatedPipe
            );

            withdrawPrizeCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.body() == null) {
                            try {
                                ResponseBaseCast responseBaseCast = new Gson().fromJson(response.errorBody().charStream(), ResponseBaseCast.class);
                                onResponseHttp.onError(responseBaseCast.msn);
                            } catch (Exception e) {
                                ApiError responseBaseCast = new Gson().fromJson(response.errorBody().charStream(), ApiError.class);
                                onResponseHttp.onError(responseBaseCast.getMessage());
                            }
                            return;
                        }
                        ResponseBaseCast responseBaseCast = new Gson().fromJson(response.body().string(), ResponseBaseCast.class);

                        onResponseHttp.onResponse(responseBaseCast);
                    } catch (Exception e) {
                        ApiError responseBaseCast = new Gson().fromJson(response.errorBody().charStream(), ApiError.class);
                        onResponseHttp.onError(responseBaseCast);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    onResponseHttp.onError(t.getMessage());
                }
            });


        } catch (Exception e) {
            onResponseHttp.onError(e.getMessage());
        }
    }


    public void getPrizesLeveBy(String level, int pager, int limit, OnResponseHttp onResponseHttp) {
        try {

            Call<ResponseBody> getprizes = this.serverConfiguration.getAPI().getprizesBy(
                    this.serverConfiguration.getToken(),
                    level, pager, limit
            );
            getprizes.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.body() == null) {
                            onResponseHttp.onError(response.errorBody());
                            return;
                        }
                        PrizesCast prizesCast = new Gson().fromJson(response.body().string(), PrizesCast.class);
                        onResponseHttp.onResponse(prizesCast);
                    } catch (Exception e) {
                        onResponseHttp.onError(e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    onResponseHttp.onError(t.getMessage());
                }
            });
        } catch (Exception e) {
            onResponseHttp.onError(e.getMessage());
        }
    }


    public void getprizes(String filter, int pager, int limit, OnResponseHttp onResponseHttp) {
        try {

            Call<ResponseBody> getprizes = this.serverConfiguration.getAPI().getprizes(
                    this.serverConfiguration.getToken(),
                    filter, pager, limit
            );
            getprizes.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.body() == null) {
                            onResponseHttp.onError(response.errorBody());
                            return;
                        }
                        PrizesCast prizesCast = new Gson().fromJson(response.body().string(), PrizesCast.class);
                        onResponseHttp.onResponse(prizesCast);
                    } catch (Exception e) {
                        onResponseHttp.onError(e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    onResponseHttp.onError(t.getMessage());
                }
            });
        } catch (Exception e) {
            onResponseHttp.onError(e.getMessage());
        }
    }


    public void updateAvatar(String realPathFromURI, String uuid, OnResponseHttp onResponseHttp) {

        try {

            File file = new File(realPathFromURI);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(realPathFromURI);
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
                    fos.write(bitmapdata);
                    fos.flush();
                    fos.close();
                    if (Utils.isNetworkAvailable(context) && Utils.isOnline()) {
                        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), f);
                        MultipartBody.Part body =
                                MultipartBody.Part.createFormData("photo", f.getName(), reqFile);

                        try {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    Call<ResponseBody> call = serverConfiguration.getAPI().updateAvatar(
                                            serverConfiguration.getToken(),
                                            Integer.parseInt(uuid), body
                                    );


                                    String response = null;
                                    try {
                                        response = call.execute().body().string();
                                        onResponseHttp.onResponse(response);

                                    } catch (IOException e) {
                                        e.printStackTrace();
                                        onResponseHttp.onError(e.getMessage());
                                    }

                                }
                            }).start();


                        } catch (Exception e) {
                            onResponseHttp.onError(e.getMessage());
                        }
                    } else {
                        onResponseHttp.onError("Sem Conecção Con A Internet . Tente Mais Tarde.");
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    onResponseHttp.onError(e.getMessage());
                }
            }
        } catch (Exception e) {
            onResponseHttp.onError(e.getMessage());
        }


    }


    // savesTheInformationAndBringsIfThereAreNewOperationsAvailable


    public void savesTheInformationAndBringsIfThereAreNewOperationsAvailable(Map<String, String> parameters, OnResponseHttp onResponseHttp) {
        try {
            String currentDate = CommonFunctions.getDate();
            String idUserPartenerLogged = parameters.get("idUserPartenerLogged");
            String operationLocalStorege = parameters.get("operationLocalStorege");
            String routerOperationLocalStorege = parameters.get("routerOperationLocalStorege");
            String operationStatusLocalStorege = parameters.get("operationStatusLocalStorege");
            this.serverConfiguration.getAPI().savesTheInformationAndBringsIfThereAreNewOperationsAvailable(
                    this.serverConfiguration.getToken(),
                    currentDate,
                    idUserPartenerLogged,
                    operationLocalStorege,
                    routerOperationLocalStorege,
                    operationStatusLocalStorege
            ).enqueue(new Callback<ResponseBody>() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.body() == null) {
                            onResponseHttp.onError(response.errorBody());
                            return;
                        }
                        AppOpeationsRepository operationModelRepository = new AppOpeationsRepository(context);
                        SavesTheInformationAndBringsIfThereAreNewOperationsAvailable datesReceivedFromApi;
                        datesReceivedFromApi = new Gson().fromJson(response.body().string(), SavesTheInformationAndBringsIfThereAreNewOperationsAvailable.class);
                        List<AvailableOperationsComposition> availableOperations = datesReceivedFromApi.getAvailableOperations();
                        for (AvailableOperationsComposition op : availableOperations) {
                            OperationModelRepository operationEntityRepository = new OperationModelRepository();
                            operationEntityRepository.setRouterMap(new Gson().toJson(op.routerMap));
                            operationEntityRepository.setZonasJson(new Gson().toJson(op.zonas));
                            operationEntityRepository.setRouterJson(new Gson().toJson(op.router));
                            operationEntityRepository.setOperationID(op.id);
                            operationEntityRepository.setStoreID(op.store_id);
                            operationEntityRepository.setRouterID(op.router.id);
                            operationEntityRepository.setParternID(0);
                            OperationModelRepository operations = (OperationModelRepository) operationModelRepository.getBy(operationEntityRepository);
                            if (operations.getId() == null || operations.getId() == 0)
                                operationModelRepository.create(operationEntityRepository);

                        }

                    } catch (Exception e) {
                        new WriteFilesSd(onResponseFile).generateNoteOnSD("Error.txt", String.format("%s", e.getMessage()));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    new WriteFilesSd(onResponseFile).generateNoteOnSD("Error.txt", String.format("%s", t.getMessage()));
                }
            });


        } catch (Exception e) {
            onResponseHttp.onError(e.getMessage());
        }
    }


    @Override
    public void handleError() {
        onResponseFile = new OnResponseFile() {
            @Override
            public void onResult(boolean isError, Object msn) {
                System.out.println(Resources.DEBUG + " " + msn);
            }
        };
    }

    public void closeOperationPartners(UserLoginCast userLoginCast, String operations, OnResponseHttp onResponseHttp) {

        this.serverConfiguration.getAPI().closeOperationPartners(userLoginCast.getToken(),operations)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        System.out.println(response);
                        onResponseHttp.onResponse("");
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        System.out.println(t.getMessage());
                        onResponseHttp.onError("");
                    }
                });
    }


    public void uploadReceiptMp3(UploadFile uploadFile, Audio amostra, UserLoginCast userLoginCast, OperationModelRepository operationModelRepository, MultipartBody.Part requestFile, String token, String latLng) {
        Date dataHoraAtual = new Date();
        String datetime = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(dataHoraAtual);
         this.serverConfiguration.getAPI().uploadReceiptMp3(
                 token,String.valueOf(operationModelRepository.getOperationID()),
                 String.valueOf(operationModelRepository.getParternID()),
                 datetime,
                 latLng,
                 requestFile
        ).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    if(response.body() == null){
                        uploadFile.response(false,0,null);
                    }else{
                        uploadFile.response(true,amostra.id,amostra.audio);
                    }
                  }catch (Exception e){
                    uploadFile.response(false,0,null);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                uploadFile.response(false,0,null);
            }
        });


    }
}
