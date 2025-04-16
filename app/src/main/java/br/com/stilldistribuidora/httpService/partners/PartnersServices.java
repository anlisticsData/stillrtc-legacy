package br.com.stilldistribuidora.httpService.partners;

import android.content.res.Resources;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import br.com.stilldistribuidora.common.CastResponseApi;
import br.com.stilldistribuidora.partners.Casts.UserLoginCast;
import br.com.stilldistribuidora.stillrtc.R;
import br.com.stilldistribuidora.stillrtc.api.ApiClient;
import br.com.stilldistribuidora.stillrtc.api.ApiEndpointInterface;
import br.com.stilldistribuidora.stillrtc.db.dataaccess.ConfigDataAccess;
import br.com.stilldistribuidora.stillrtc.db.models.Config;
import br.com.stilldistribuidora.stillrtc.db.models.RouterApiDirection;
import br.com.stilldistribuidora.stillrtc.db.models.RouterApiDirectionContainer;
import br.com.stilldistribuidora.stillrtc.db.models.StateOperation;
import br.com.stilldistribuidora.stillrtc.interfac.OnApiResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PartnersServices {

    private static ApiEndpointInterface API;
    private String internalApiToken;
    private String url;
    private ConfigDataAccess confDataAccess;
    private String partnerId;
    private String jwt;



    public PartnersServices(ConfigDataAccess confDataAccess){
        this.confDataAccess = confDataAccess;
        try {
            API = ApiClient.getClient(this.url.trim()).create(ApiEndpointInterface.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public PartnersServices(ConfigDataAccess confDataAccess, Config userLogged, Config endPointAccess, Config token, Config internalApiToken) {
        this.confDataAccess = confDataAccess;
        try {

            this.partnerId = (new JSONObject(userLogged.getDataJson())).getString("uuid");
            this.jwt = token.getDataJson();
            this.url = endPointAccess.getDataJson();
            this.internalApiToken = internalApiToken.getDataJson();
            API = ApiClient.getClient(this.url.trim()).create(ApiEndpointInterface.class);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }






    //usersSetOperationStatus

    //jwt,partnerId,routerId,operationId,"1",
    public List<Object> usersSetOperationStatus(String routerId, String operationId, String state, String refusal, OnApiResponse onResponse) {
        List<Object> verifiedOperationList = new ArrayList<>();
        try {

            API.updateUsersOperationStatus(this.internalApiToken, this.partnerId, routerId, operationId, state, refusal)
                    .enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            CastResponseApi castResponseApi = new CastResponseApi();
                            if (response.isSuccessful()) {
                                try {
                                    JSONObject dataReceived = new JSONObject(response.body().string());
                                    if (dataReceived.getInt("status") != 200) {
                                        castResponseApi.setState(dataReceived.getInt("status"));
                                        castResponseApi.setMensage(dataReceived.getString("msn"));
                                        onResponse.onError(castResponseApi);
                                    } else {
                                        castResponseApi.setState(dataReceived.getInt("status"));
                                        castResponseApi.setMensage(dataReceived.getString("msn"));
                                        onResponse.onSucess(castResponseApi);
                                    }
                                } catch (Exception e) {
                                    castResponseApi.setState(500);
                                    castResponseApi.setMensage(e.getMessage());
                                    onResponse.onError(castResponseApi);
                                }
                            } else {
                                onResponse.onError(castResponseApi);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            CastResponseApi castResponseApi = new CastResponseApi();
                            castResponseApi.setState(500);
                            castResponseApi.setMensage(t.getMessage());
                            onResponse.onError(castResponseApi);
                        }
                    });


        } catch (Exception e) {
            onResponse.onError("");
        }
        return verifiedOperationList;

    }

    public List<Object> checkServiceOperation(String operationsIds, OnApiResponse onResponse) {
        List<Object> verifiedOperationList = new ArrayList<>();

        try {
            API.validateStatusOfOperations(this.internalApiToken, operationsIds, this.partnerId).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject dataResponse = new JSONObject(response.body().string());
                            if (dataResponse.getInt("status") == 200) {
                                JSONArray data = dataResponse.getJSONArray("data");
                                List<StateOperation> stateOperation = new ArrayList<>();
                                for (int next = 0; next < data.length(); next++) {
                                    stateOperation.add(
                                            new StateOperation(
                                                    data.getJSONObject(next).get("operation").toString(),
                                                    data.getJSONObject(next).get("router").toString(),
                                                    data.getJSONObject(next).get("state").toString(),
                                                    data.getJSONObject(next).get("device_id").toString()
                                            ));
                                }
                                onResponse.onSucess(stateOperation);
                            }
                        } catch (Exception e) {
                            onResponse.onError(e.getMessage());
                        }
                    } else {
                        onResponse.onError("");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    onResponse.onError(t);
                }
            });

        } catch (Exception e) {
        }
        return verifiedOperationList;
    }

    public List<Object> directionGoogleApi(String origin, String destiny, OnApiResponse onResponse) {
        List<Object> verifiedOperationList = new ArrayList<>();

        try {
            API.traceRouteInGoogleApiWithLatitudeAndLongitude(this.internalApiToken, origin, destiny).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            JSONObject dataResponse = new JSONObject(response.body().string());
                            if (dataResponse.getInt("status") == 200
                                    && dataResponse.has("data") && !dataResponse.isNull("data")) {
                                JSONObject data = dataResponse.getJSONObject("data");
                                JSONObject distance = data.getJSONObject("distance");
                                JSONObject duration = data.getJSONObject("duration");
                                JSONArray router = data.getJSONArray("router");
                                List<RouterApiDirection> routerApiDirections = new ArrayList<>();
                                for (int next = 0; next < router.length(); next++) {
                                    String latitude = router.getJSONObject(next).getJSONObject("end_location").getString("lat");
                                    String logitude = router.getJSONObject(next).getJSONObject("end_location").getString("lng");
                                    String tipoInstrucao = router.getJSONObject(next).getString("html_instructions");
                                    routerApiDirections.add(new RouterApiDirection(latitude, logitude, tipoInstrucao));
                                }
                                RouterApiDirectionContainer routerApiDirectionContainer = new RouterApiDirectionContainer();
                                routerApiDirectionContainer.setRouterApiDirection(routerApiDirections);
                                routerApiDirectionContainer.setDistanciaText(distance.getString("distanceText"));
                                routerApiDirectionContainer.setDistanciaValue(distance.getString("distanceMetres"));
                                routerApiDirectionContainer.setDuracaoText(duration.getString("durationText"));
                                routerApiDirectionContainer.setDuracaoValue(duration.getString("durationMs"));
                                onResponse.onSucess(routerApiDirectionContainer);
                            } else {
                                onResponse.onError("Erro ao Calcular a Rotas.");
                            }
                        } catch (Exception e) {
                            onResponse.onError(e.getMessage());
                        }
                    } else {
                        onResponse.onError("");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    onResponse.onError(t.getMessage());
                }
            });

        } catch (Exception e) {
            onResponse.onError(e.getMessage());
        }
        return verifiedOperationList;
    }


    public void partnerCompletedTheTransaction(String operation, String irPartrner, String deviceId, int router_id, OnApiResponse responseHttp) {
        try {
            Call<ResponseBody> responseBodyCall = API.
                    partnerCompletedTheTransaction(this.internalApiToken, operation, irPartrner, deviceId, String.valueOf(router_id));

            responseBodyCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try{
                        if(response.isSuccessful()){
                            JSONObject dataResponse = new JSONObject(response.body().string());

                            System.out.println("");

                        }else{
                            responseHttp.onError(R.string.str_error_operations_finish);
                        }
                    }catch (Exception e){
                        responseHttp.onError(e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    responseHttp.onError(t.getMessage());
                }
            });

        } catch (Exception e) {
            responseHttp.onError(e.getMessage());
        }
    }
}
