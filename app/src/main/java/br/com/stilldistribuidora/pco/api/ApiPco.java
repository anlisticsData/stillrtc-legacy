package br.com.stilldistribuidora.pco.api;

import com.google.android.gms.common.api.Api;

import br.com.stilldistribuidora.stillrtc.api.ApiEndpointInterface;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiPco {

    /*
    public static   final String BASE_URL = "http://stillrtc.com/api/";
    public static final String BASE_BACKOFFICE = "http://stillrtc.com/";
    public static final String HEADER = "X-APPLICATION-TOKEN:dHVkbyBtZSDDqSBsw61jaXRvIG1hcyBuZW0gdHVkbyBtZSBjb252w6lt_";

*/
    

/*
    public static final String BASE_URL = "http://apigrafica/api/";
    public static final String BASE_BACKOFFICE = "http://apigrafica/";
*/





    public static final String BASE_URL = "http://stillpco.com/api/";
    public static final String BASE_BACKOFFICE = "http://stillpco.com/api/";



//
//    public static final String  IP="192.168.15.188";
//    public static final String BASE_URL = "http://"+IP+"/pco/api/";
//    public static final String BASE_BACKOFFICE = "http://"+IP+"/pco/";


/*
    public static final String BASE_URL = "http://192.168.0.123/pco/api/";
    public static final String BASE_BACKOFFICE = "http://192.168.0.123/pco/";

*/


    public static final String HEADER = "X-APPLICATION-TOKEN:";
    public static final String API_PATH_PRIVATE_INSTALL = "d217d774c31527d61ebb3a4890421ab6a31b66d8";
    private static Retrofit retrofit = null;
    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl( BASE_URL )
                    .addConverterFactory( GsonConverterFactory.create() )
                    .build();
        }
        return retrofit;
    }
    //Implements
    public ApiPcoInterface api() {
        return getClient().create( ApiPcoInterface.class );
    }
    public ApiPcoInterface ws_app_generats() {
        return getClient().create( ApiPcoInterface.class );
    }
    public ApiPcoInterface ws_get_token() {
        return getClient().create( ApiPcoInterface.class );
    }

    public ApiPcoInterface ws_get_token_api() {
        return getClient().create( ApiPcoInterface.class );
    }

    public ApiPcoInterface ws_gf_login_parceiro() {
        return getClient().create( ApiPcoInterface.class );
    }

    public ApiPcoInterface get_material_grafica_data_status() {
        return getClient().create( ApiPcoInterface.class );
    }

    public ApiPcoInterface start_retiradas_grafica(){
        return getClient().create( ApiPcoInterface.class );
    }
    public ApiPcoInterface get_material_em_grafica_by(){
        return getClient().create( ApiPcoInterface.class );
    }

    public ApiPcoInterface save_retiradas_grafica_finish(){
        return getClient().create( ApiPcoInterface.class );
    }


    public ApiPcoInterface save_retiradas_grafica_fotos_app(){
        return  getClient().create(ApiPcoInterface.class);
    }


    public ApiPcoInterface ws_gf_status_operation(){
        return  getClient().create(ApiPcoInterface.class);
    }


    public ApiPcoInterface ws_gf_status_operation_set(){
        return getClient().create(ApiPcoInterface.class);
    }


    public  ApiPcoInterface updateGraphicsOperationTime(){
        return  getClient().create(ApiPcoInterface.class);

    }


    public  ApiPcoInterface savePartnerPosition(){
        return  getClient().create(ApiPcoInterface.class);
    }

    public  ApiPcoInterface blockDownloadedRecord(){
        return getClient().create(ApiPcoInterface.class);
    }







}
