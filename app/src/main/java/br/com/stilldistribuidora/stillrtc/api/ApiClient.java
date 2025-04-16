package br.com.stilldistribuidora.stillrtc.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Still Technology and Development Team on 20/04/2017.
 */

public class ApiClient {


    public static   final String BASE_URL = "http://192.168.0.50/still/api/";
    public static final String BASE_BACKOFFICE = "http://192.168.0.50/still/rtc/";
    public static final String HEADER = "X-APPLICATION-TOKEN:dHVkbyBtZSDDqSBsw61jaXRvIG1hcyBuZW0gdHVkbyBtZSBjb252w6lt_";
    public static final String HEADERx = "dHVkbyBtZSDDqSBsw61jaXRvIG1hcyBuZW0gdHVkbyBtZSBjb252w6lt_";




    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


    public static Retrofit getClient(String API_BASE_URL) {
        OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(600, TimeUnit.SECONDS)
                .readTimeout(600, TimeUnit.SECONDS)
                .writeTimeout(600, TimeUnit.SECONDS)
                .cache(null)
                .build();

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }


}