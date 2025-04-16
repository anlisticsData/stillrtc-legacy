package br.com.stilldistribuidora.stillrtc.api;

/**
 * Created by Still Technology and Development Team on 20/04/2017.
 *
 *


 /*
 @FormUrlEncoded
 @Headers({ApiClient.HEADER})
 @POST("loginDevice")
 Call<Device.Result> autenticarDispositivo(
 @Field("identifier") String identifier,
 @Field("password") String password,
 @Field("type") int type);


 */



//X-APPLICATION-TOKEN

//Alterar Todas as Chamadas do App para o Novo padrao




import br.com.stilldistribuidora.stillrtc.db.models.Device;
import br.com.stilldistribuidora.stillrtc.db.models.DeviceStatus;
import br.com.stilldistribuidora.stillrtc.db.models.Operation;
import br.com.stilldistribuidora.stillrtc.db.models.Picture;
import br.com.stilldistribuidora.stillrtc.db.models.Prefs;
import br.com.stilldistribuidora.stillrtc.db.models.Store;
import br.com.stilldistribuidora.stillrtc.db.models.User;
import br.com.stilldistribuidora.stillrtc.db.models.Zone;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiEndpointInterface {







    @FormUrlEncoded
    @POST("validateUserInformation")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<ResponseBody> validateUserInformation(
            @Field("email") String email,
            @Field("cpfcnpj") String cpfcnpj
    );

    @FormUrlEncoded
    @POST("updatePartnerPassword")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<ResponseBody> updatePartnerPassword(
            @Field("email") String email,
            @Field("cpfcnpj") String cpfcnpj,
            @Field("newpassword") String newPassword
    );











    @FormUrlEncoded
    @POST("requestDeviceCode")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<ResponseBody> requestDeviceCode(
            @Header("X-APPLICATION-TOKEN") String token,
            @Field("deliveryId") String deliveryId,
            @Field("partnerId") String partnerId
           );



    @FormUrlEncoded
    @POST("cancelPartnerOperation")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<ResponseBody> cancelPartnerOperation(
            @Header("X-APPLICATION-TOKEN") String token,
            @Field("partnerId") String partnerId,
            @Field("deliveryId") String deliveryId,
            @Field("justification") String justification);




    @FormUrlEncoded
    @POST("registerAccepted")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<ResponseBody> registerAccepted(
            @Header("X-APPLICATION-TOKEN") String token,
            @Field("deliveryId") String deliveryId,
            @Field("partnerId") String partnerId,
            @Field("routerId") String routerId);






    @FormUrlEncoded
    //@POST("aaaaa")
    @POST("operationsNearAndFarBy")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<ResponseBody> operationsNearAndFarBy(
            @Header("X-APPLICATION-TOKEN") String token,
            @Field("address") String address,
            @Field("type") String type,
            @Field("partner") String partner,
            @Field("date") String date,
            @Field("loadedOperations") String loadedOperations );





    //27-07-2022


    @FormUrlEncoded
    @POST("updatePartnerInformations")
    Call<ResponseBody> updatePartnerInformations(
            @Header("X-APPLICATION-TOKEN") String token,
            @Field("idpartrner") String idpartrner,
            @Field("name") String name,
            @Field("last_name") String last_name,
            @Field("moblie_phone") String moblie_phone,
            @Field("email") String email,
            @Field("address") String address,
            @Field("type") String type,
            @Field("name_completed") String name_completed,
            @Field("cpf") String cpf,
            @Field("name_bank") String name_bank,
            @Field("agency") String agency,
            @Field("conta") String conta,
            @Field("type_pix") String type_pix,
            @Field("pix") String pix);



    @FormUrlEncoded
    @POST("withdrawPrize")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<ResponseBody> withdrawPrize(
            @Header("X-APPLICATION-TOKEN") String token,
            @Field("parternId") String parternId,
            @Field("stillCoinsUser") String stillCoinsUser,
            @Field("prizes") String prizes
    );

    @FormUrlEncoded
    @POST("checkOperationAvailablePartner")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<ResponseBody> issueOperationAlertAvailable(
            @Header("X-APPLICATION-TOKEN") String token,
            @Field("partnerId") String partnerId,
            @Field("operatingRangePerDays") String operatingRangePerDays

    );






    @FormUrlEncoded
    @POST("getPrizesLeveBy")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<ResponseBody> getprizesBy(
            @Header("X-APPLICATION-TOKEN") String token,
            @Field("level") String level,
            @Field("pager") int pager,
            @Field("limit") int limit
    );


    @FormUrlEncoded
    @POST("getprizes")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<ResponseBody> getprizes(
            @Header("X-APPLICATION-TOKEN") String token,
            @Field("filter") String filter,
            @Field("pager") int pager,
            @Field("limit") int limit
            );



    //26-07-2022


    @Multipart
    @POST("updateAvatar")
    Call<ResponseBody> updateAvatar(
            @Header("X-APPLICATION-TOKEN") String token,
            @Part("partenerid") int partener_id,
            @Part MultipartBody.Part photo);



    @FormUrlEncoded
    @POST("savesTheInformationAndBringsIfThereAreNewOperationsAvailable")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<ResponseBody> savesTheInformationAndBringsIfThereAreNewOperationsAvailable(
            @Header("X-APPLICATION-TOKEN") String token,
            @Field("searchData") String searchData,
            @Field("partnerId") String partnerId,
            @Field("operationLocalStorege") String operationLocalStorege,
            @Field("routerOperationLocalStorege") String routerOperationLocalStorege,
            @Field("operationStatusLocalStorege") String operationStatusLocalStorege);



    @FormUrlEncoded
    @POST("partnerCompletedTheTransaction")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<ResponseBody> partnerCompletedTheTransaction(
            @Header("X-APPLICATION-TOKEN") String token,
            @Field("delivery_id") String delivery_id,
            @Field("partern_id") String partern_id,
            @Field("device_id") String device_id,
            @Field("router_id") String router_id);




    @FormUrlEncoded
    @POST("traceRouteInGoogleApiWithLatitudeAndLongitude")
    Call<ResponseBody> traceRouteInGoogleApiWithLatitudeAndLongitude(
            @Header("X-APPLICATION-TOKEN") String token,
            @Field("origin") String origin,
            @Field("destiny") String destiny
          );

    ///05-05-2022
    @FormUrlEncoded
    @POST("updateUsersOperationStatus")
    Call<ResponseBody> updateUsersOperationStatus(
            @Header("X-APPLICATION-TOKEN") String token,
            @Field("idpartner") String idpartner,
            @Field("idrouter") String idrouter,
            @Field("idoperation") String idoperation,
            @Field("partner_status") String partner_status,
            @Field("refusal") String refusal);


    @FormUrlEncoded
    @POST("validateStatusOfOperations")
    Call<ResponseBody> validateStatusOfOperations(
            @Header("X-APPLICATION-TOKEN") String token,
            @Field("operations") String operations,
            @Field("idPartnerInt") String idPartnerInt);



    //19-04-2022
    @FormUrlEncoded
    @POST("authentication")
    Call<Device.Result> authentication(
            @Header("device-id") String deviceUuid,
            @Header("X-APPLICATION-TOKEN") String token,
            @Field("identifier") String identifier,
            @Field("password") String password,
            @Field("type") int type);


    @FormUrlEncoded
    @POST("searchingByAddress")
    Call<ResponseBody> searchingByAddress(
            @Header("X-APPLICATION-TOKEN") String token,
            @Field("address") String address);

    //15-03-2022


    @FormUrlEncoded
        @POST("registerPartners")
        Call<ResponseBody> registerPartners(
                @Header("X-APPLICATION-TOKEN") String token,
                @Field("name") String name,
                @Field("last_name") String last_name,
                @Field("moblie_phone") String moblie_phone,
                @Field("cpf") String cpf,
                @Field("email") String email,
                @Field("password") String password,
                @Field("terms") String terms,
                @Field("address") String address,
                @Field("pix") String pixjson);




    @FormUrlEncoded
    @POST("loginDevice")
    Call<Device.Result> autenticarDispositivo(
            @Header("device-id") String deviceUuid,
            @Header("X-APPLICATION-TOKEN") String token,
            @Field("identifier") String identifier,
            @Field("password") String password,
            @Field("type") int type);



    @FormUrlEncoded
    @POST("loginParceiro")
    Call<ResponseBody> loginParceiro(
            @Header("X-APPLICATION-TOKEN") String token,
            @Field("email") String email,
            @Field("password") String password);




    @FormUrlEncoded
    @POST("closeOperationPartners")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<ResponseBody> closeOperationPartners(
            @Header("X-APPLICATION-TOKEN") String token,
            @Field("operations") String operations

    );


    @FormUrlEncoded
    @POST("operationsNearAndFar")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<ResponseBody> operationsNearAndFar(
            @Header("X-APPLICATION-TOKEN") String token,
            @Field("address") String address,
            @Field("type") String type,
            @Field("partner") String partner);




    @FormUrlEncoded
    @POST("usersSetOperationStatus")
    @Headers("Content-Type: application/x-www-form-urlencoded")
    Call<ResponseBody> usersSetOperationStatus(
            @Header("X-APPLICATION-TOKEN") String token,
            @Field("idpartner") String idpartner,
            @Field("idrouter") String idrouter,
            @Field("idoperation") String idoperation,
            @Field("partner_status") String partners_status,
            @Field("authority") String authoruty);








    @FormUrlEncoded
    @POST("login")
    Call<User.Result> autenticarUsuario(
            @Header("device-id") String deviceUuid,
            @Header("X-APPLICATION-TOKEN") String token,
            @Field("email") String email,
            @Field("password") String password,
            @Field("type") int type);




    @FormUrlEncoded
    @POST("getOperationSupervisorUltimoPing")
    Call<ResponseBody> getOperationSupervisorUltimoPing(
            @Header("device-id") String deviceUuid,
            @Header("X-APPLICATION-TOKEN") String token,
            @Header("device-id-jwt") String jwt,
            @Field("operationKey") String operationkey,
            @Field("dispositivokey") String dispositivoKey);



    @FormUrlEncoded
    @POST("getOperationSupervisorGrupo")
    Call<ResponseBody> getOperationSupervisorGrupo(
            @Header("device-id") String deviceUuid,
            @Header("X-APPLICATION-TOKEN") String token,
            @Header("device-id-jwt") String jwt,
            @Field("supervisorKeyAtivo") String identifier);



    @FormUrlEncoded
    @POST("getCheckoutDispositivoOperation")
    Call<ResponseBody> getCheckoutDispositivoOperation(
            @Header("device-id") String deviceUuid,
            @Header("X-APPLICATION-TOKEN") String token,
            @Header("device-id-jwt") String jwt,
            @Field("keyOperacaoId") String identifier);





    @GET("operations")
    Call<Operation.Result> recuperarOperacoesPorIdentificador(
            @Header("device-id") String deviceUuid,
            @Header("X-APPLICATION-TOKEN") String token,
            @Header("device-id-jwt") String jwt,
            @Query("identifier") String identifier, @Query("date") String dateSearch);



    @FormUrlEncoded
    @POST("checkoutOperacaoStatus")
    Call<ResponseBody> checkoutOperacaoStatus(
            @Header("device-id") String deviceUuid,
            @Header("X-APPLICATION-TOKEN") String token,
            @Header("device-id-jwt") String jwt,
            @Field("delivery_id") String delivery_id,
            @Field("date") String dateSearch);





    @FormUrlEncoded
    @POST("deviceStatusPartner")
    Call<ResponseBody> registrarStatusDispositivoPartner(
            @Header("device-id") String deviceUuid,
            @Header("X-APPLICATION-TOKEN") String token,
            @Header("device-id-jwt") String jwt,
            @Field("latitude") double latitude,
            @Field("longitude") double longitude,
            @Field("batery") float battery,
            @Field("ip") String ip,
            @Field("device_identifier") String device_identifier,
            @Field("delivery_fragment_id") int delivery_fragment_id,
            @Field("created_at") String created_at,
            @Field("laps") String lap
    );




    @FormUrlEncoded
    @POST("device-status")
    Call<DeviceStatus.Result> registrarStatusDispositivo(
            @Header("device-id") String deviceUuid,
            @Header("X-APPLICATION-TOKEN") String token,
            @Header("device-id-jwt") String jwt,
            @Field("latitude") double latitude,
            @Field("longitude") double longitude,
            @Field("batery") float battery,
            @Field("ip") String ip,
            @Field("device_identifier") String device_identifier,
            @Field("delivery_fragment_id") int delivery_fragment_id,
            @Field("created_at") String created_at
    );


    @FormUrlEncoded
    @POST("registerActivityOnMapByUser")
    Call<ResponseBody> registerActivityOnMapByUser(
           @Header("X-APPLICATION-TOKEN") String token,
                    @Field("history") String history
            );


    @FormUrlEncoded
    @POST("registerEndOperation")
    Call<Operation.Result> registrarFimOperacao(
            @Header("device-id") String deviceUuid,
            @Header("X-APPLICATION-TOKEN") String token,
            @Header("device-id-jwt") String jwt,
            @Field("delivery_id") int delivery_id,
            @Field("device_id") int device_id,
            @Field("date_end") String date_end);


    //define a regra de 50+1 , dispositivos Finalizados
    //registerEndOperationOf50Plus1
    //11052022

    @FormUrlEncoded
    @POST("registerEndOperationOf50Plus1")
    Call<Operation.Result> registerEndOperationOf50Plus1(
            @Header("device-id") String deviceUuid,
            @Header("X-APPLICATION-TOKEN") String token,
            @Header("device-id-jwt") String jwt,
            @Field("delivery_id") int delivery_id,
            @Field("device_id") int device_id,
            @Field("date_end") String date_end);


    @FormUrlEncoded
    @POST("changePassword")
    Call<ResponseBody> alterarSenhaDispositivoAPI(
            @Header("device-id") String deviceUuid,
            @Header("X-APPLICATION-TOKEN") String token,
            @Header("device-id-jwt") String jwt,
            @Field("uuid") String uuid,
            @Field("old_password") String pwd_current,
            @Field("new_password") String pwd_new);



    @GET("versionapp")
    Call<ResponseBody> recuperarVersaoApp(@Header("device-id") String deviceUuid,
                                          @Header("X-APPLICATION-TOKEN") String token,
                                          @Header("device-id-jwt") String jwt);





    @Multipart
    @POST("registerPicturePartners")
    Call<ResponseBody> registerPicturePartners(
            @Header("device-id") String deviceUuid,
            @Header("X-APPLICATION-TOKEN") String token,
            @Header("device-id-jwt") String jwt,
            @Part("created_at") String foo,
            @Part("latitude") double latitude,
            @Part("longitude") double longitude,
            @Part("type") int type,
            @Part("device_id") int device_id,
            @Part("delivery_id") int delivery_id,
            @Part MultipartBody.Part file);

    @Multipart
    @POST("registerPicture")
    Call<Picture.Result> registrarFotoDevice(
            @Header("device-id") String deviceUuid,
            @Header("X-APPLICATION-TOKEN") String token,
            @Header("device-id-jwt") String jwt,
            @Part("created_at") String foo,
            @Part("latitude") double latitude,
            @Part("longitude") double longitude,
            @Part("type") int type,
            @Part("device_id") int device_id,
            @Part("delivery_id") int delivery_id,
            @Part MultipartBody.Part file);





    @GET("stores/30,0/{client_id}")
    Call<Store.Result> getAllStoreByClient(
            @Header("device-id") String deviceUuid,
            @Header("X-APPLICATION-TOKEN") String token,
            @Header("device-id-jwt") String jwt,
            @Path("client_id") int client_id);



    @GET("prefsapp")
    Call<Prefs.Result> resgatarPreferencias(
            @Header("device-id") String deviceUuid,
            @Header("X-APPLICATION-TOKEN") String token,
            @Header("device-id-jwt") String jwt
    );

    @FormUrlEncoded
    @POST("stores/30,0")
    Call<Store.Result> getAllStoreByClient(
            @Header("device-id") String deviceUuid,
            @Header("X-APPLICATION-TOKEN") String token,
            @Header("device-id-jwt") String jwt,
            @Field("client_id") int client_id,
            @Field("status") int status,
            @Field("search") String search
    );


    @GET("deliveries/50,0/{store_id}")
    Call<Operation.Result> getAllDeliveriesBy(
            @Header("device-id") String deviceUuid,
            @Header("X-APPLICATION-TOKEN") String token,
            @Header("device-id-jwt") String jwt,
            @Path("store_id") int storeId, @Query("date") String dateSearch);


    @POST("zonesByIds/{ids}")
    Call<Zone.Result> resgatarZonasPorIdsAPI(
            @Header("device-id") String deviceUuid,
            @Header("X-APPLICATION-TOKEN") String token,
            @Header("device-id-jwt") String jwt,
            @Path("ids") String ids);



    @POST("zonesBysIdsSimultanias")
    Call<ResponseBody> zonesBysIdsSimultaniasAPI(
            @Header("device-id") String deviceUuid,
            @Header("X-APPLICATION-TOKEN") String token,
            @Header("device-id-jwt") String jwt,
            @Path("ids") String ids);



    @FormUrlEncoded
    @POST("searchforcabinaudio")
    Call<ResponseBody> searchforcabinaudio(
            @Header("X-APPLICATION-TOKEN") String token,
            @Field("code_delivery") String code_delivery);



    @Multipart
    @POST("record-customer-audio")
    Call <ResponseBody>uploadReceiptMp3(
            @Header("X-APPLICATION-TOKEN") String token,
            @Part("code_delivery") String code,
            @Part("code_partner") String code_partner,
            @Part("time_device") String time_device,
            @Part("lat_lng") String lat_lng,
            @Part  MultipartBody.Part  audio
    );







}