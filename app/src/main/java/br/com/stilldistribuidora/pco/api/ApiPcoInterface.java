package br.com.stilldistribuidora.pco.api;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiPcoInterface {


    @GET("api")
    Call<ResponseBody> api();



    @FormUrlEncoded
    @POST("ws_app_generats")
    Call<ResponseBody> ws_app_generats(
            @Field("token_session") String token_session);


    @FormUrlEncoded
    @POST("ws_get_token_api")
    Call<ResponseBody> ws_get_token_api(
            @Header("X-APPLICATION-TOKEN") String tokenApi,
            @Field("request") String request);




    @FormUrlEncoded
    @POST("ws_get_token")
    Call<ResponseBody> ws_get_token(
            @Header("X-APPLICATION-TOKEN") String tokenApi,
            @Field("request") String request);


///////////////////////////////////////////////////////////////////////

    @FormUrlEncoded
    @POST("ws_gf_login_parceiro")
    Call<ResponseBody> ws_gf_login_parceiro(
            @Field("token_session") String token_session,
            @Field("device") String device,
            @Field("user") String user,
            @Field("pasword") String pasword);




    @FormUrlEncoded
    @POST("get_material_em_grafica_by")
    Call<ResponseBody> get_material_em_grafica_by(
            @Field("token_session") String token_session,
            @Field("device") String device,
            @Field("id_retirada") String id_retirada,
            @Field("user_codigo") String user_codigo);



    @FormUrlEncoded
    @POST("get_material_grafica_data_status")
    Call<ResponseBody> get_material_grafica_data_status(
            @Field("token_session") String token_session,
            @Field("device") String device,
            @Field("data_busca") String data_busca,
            @Field("user_codigo") String user_codigo,
            @Field("status_busca") String status_busca);



    @FormUrlEncoded
    @POST("start_retiradas_grafica")
    Call<ResponseBody> start_retiradas_grafica(
            @Field("token_session") String token_session,
            @Field("device") String device,
            @Field("retiradas_ids") String retiradas_ids,
            @Field("retiradas_qts") String retiradas_qts,
            @Field("user_codigo") String user_codigo,
            @Field("autorizacao") String autorizacao,
            @Field("obs") String obs
    );




    //@FormUrlEncoded
    //@Headers("Content-Type: multipart/form-data")
    @Multipart
    @POST("save_retiradas_grafica_fotos")
    Call<ResponseBody> save_retiradas_grafica_fotos(
            @Part("token_session") RequestBody token_session,
            @Part("device") RequestBody device,
            @Part("retiradas_ids") RequestBody retiradas_ids,
            @Part MultipartBody.Part foto,
            @Part("user_codigo") RequestBody user_codigo,
            @Part("lat_long") RequestBody lat_long

    );


    @Multipart
    @POST("save_retiradas_grafica_fotos_app")
    Call<ResponseBody> save_retiradas_grafica_fotos_app(
            @Part MultipartBody.Part name,
            @Part MultipartBody.Part uploadedfile

    );


    //save_retiradas_grafica_finish
    /*
    $retiradas_ids = $app->request->post("retiradas_ids");
    $user = $app->request->post("user_codigo");
    $token = $app->request->post("token_session");
    $device = $app->request->post("device");
    */


    @FormUrlEncoded
    @POST("save_retiradas_grafica_finish")
    Call<ResponseBody> save_retiradas_grafica_finish(
            @Field("token_session") String token_session,
            @Field("device") String device,
            @Field("quantidade") String quantidade,
            @Field("retiradas_ids") String retiradas_ids,
            @Field("user_codigo") String user_codigo,
            @Field("data_start_off_line") String data_start_off_line


    );


    @FormUrlEncoded
    @POST("ws_gf_status_operation")
    Call<ResponseBody> ws_gf_status_operation(
            @Field("token_session") String token_session,
            @Field("user_codigo") String user_codigo,
            @Field("gf_operacao") String gf_operacao
    );


    @FormUrlEncoded
    @POST("ws_gf_status_operation_set")
    Call<ResponseBody> ws_gf_status_operation_set(
            @Field("token_session") String token_session,
            @Field("user_codigo") String user_codigo,
            @Field("gf_operacao") String gf_operacao,
            @Field("gf_status")   String gf_status
    );




    @FormUrlEncoded
    @POST("updateGraphicsOperationTime")
    Call<ResponseBody> updateGraphicsOperationTime(
            @Field("token_session") String token_session,
            @Field("grfOperacao") String grfOperacao,
            @Field("time")   String time
    );


    @FormUrlEncoded
    @POST("savePartnerPosition")
    Call<ResponseBody> savePartnerPosition(
            @Field("token_session") String token_session,
            @Field("grfOperacao") String grfOperacao,
            @Field("device")   String device,
            @Field("latLon") String latLon,
            @Field("userCodigo") String userCodigo
    );




    //block_downloaded_record


    @FormUrlEncoded
    @POST("block_downloaded_record")
    Call<ResponseBody> blockDownloadedRecord(
            @Field("token_session") String token_session,
            @Field("device")   String device,
            @Field("retiradas_ids") String retiradas_ids,
            @Field("user_codigo") String user_codigo
    );


}
