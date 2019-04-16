package com.example.webwerks.autosms.service;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    //private static final String BASE_URL = "http://180.149.240.85/auto-sms-app/public/api/v1/";
    //private static final String BASE_URL = "http://134.119.220.143/~mobilerewards/public/api/v1/";
    private static final String BASE_URL = "http://mobilerewards.mobi/api/v1/";
    private Retrofit retrofit;
    private static ApiClient apiClient;


    private ApiClient(){
        retrofit = getRetrofitInstance();
    }

    public static ApiClient getInstance(){

        if (apiClient == null){
            apiClient = new ApiClient();
        }
        return apiClient;
    }


    private Retrofit getRetrofitInstance(){

        retrofit = new Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .client(getClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    private OkHttpClient getClient(){

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);
        httpClient.connectTimeout(3, TimeUnit.MINUTES);
        httpClient.readTimeout(3,TimeUnit.MINUTES);
        httpClient.writeTimeout(3,TimeUnit.MINUTES);
        return httpClient.build();
    }

     ApiServices getApiServices(){

        return retrofit.create(ApiServices.class);
    }

}
