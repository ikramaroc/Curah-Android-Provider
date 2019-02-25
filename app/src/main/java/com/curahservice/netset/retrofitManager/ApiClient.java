package com.curahservice.netset.retrofitManager;


import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


import static com.curahservice.netset.util.Const.SERVER_REMOTE_URL;


/**
 * Created by Neeraj Narwal on 5/5/17.
 */
public class ApiClient {
    private  Retrofit retrofit = null;
    OkHttpClient client;
    public  Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = getClient();
        }
        return retrofit;
    }
    String rememberToken,versionCode;

    public   ApiClient(String RememberToken,String VersionCode){
        this.rememberToken=RememberToken;
        this.versionCode=VersionCode;
    }

    public Retrofit getClient() {
//        Log.e("token>>",authorizatonToken);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        if (rememberToken != null) {
            Log.e("rememberToken",rememberToken);
            client = new OkHttpClient.Builder().addInterceptor(interceptor)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(new Interceptor() {      // add header in api
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            Request authenticatedRequest = request.newBuilder()
                                    .header("token", rememberToken)
                                    .header("version", versionCode)
                                    .header("type", "A")
                                    .header("apptype", "S")
                                    .build();
                            return chain.proceed(authenticatedRequest);
                        }
                    })
                    .build();
        } else {
            client = new OkHttpClient.Builder().addInterceptor(interceptor)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(new Interceptor() {      // add header in api
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request request = chain.request();
                            Request authenticatedRequest = request.newBuilder()
                                    .header("version", versionCode)
                                    .header("type", "A")
                                    .header("apptype", "S")
                                    .build();
                            return chain.proceed(authenticatedRequest);
                        }
                    })
                    .build();
        }
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(SERVER_REMOTE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
        return retrofit;
    }

}



