package com.example.hoopy.Network;


import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class NetworkClient {

    public static final String LIVE_BASE_URL = "https://www.team.hoopy.in/api/1.0/testApis/";


    private static Retrofit retrofit = null;

    public static Retrofit getClient() {

        if (retrofit == null) {

            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();

            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

            OkHttpClient httpClient = new OkHttpClient.Builder()

                    .addInterceptor(loggingInterceptor)
                    .callTimeout(60, TimeUnit.SECONDS)
                    .build();



            retrofit = new Retrofit.Builder()
                    .baseUrl(LIVE_BASE_URL)
                    .client(httpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
    public static <S> S cteateService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
