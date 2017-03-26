package com.example.fwms.myapplication.base.http;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 刘宇飞 创建 on 2017/3/15.
 * 描述：
 */

public class HttpClient {
    private static HashMap<Class, Object> requestMaps = new HashMap<>();
    private static HashMap<String, Retrofit> retorfitMaps = new HashMap<>();

    private static Retrofit getRetrofit(String baseUrl) {
        if (!retorfitMaps.containsKey(baseUrl)) {
            //使用OkHttp拦截器可以指定需要的header给每一个Http请求
            OkHttpClient client = new OkHttpClient().newBuilder()
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(true)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(new LoggingInterceptor())//日志
//            .addInterceptor(new NotEdcodeLoggingInterceptor())//不加密
                    //    .addNetworkInterceptor(new RequestHeaderInterceptor())//请求头
                    .build();
            Retrofit retrofit = new Retrofit
                    .Builder()
                    .baseUrl(baseUrl)
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    //  .addConverterFactory(EncryptionGsonConvertrFactory.create())//加密
                    .addConverterFactory(GsonConverterFactory.create())//不加密
                    .client(client)
                    .build();
            retorfitMaps.put(baseUrl, retrofit);
        }

        return retorfitMaps.get(baseUrl);
    }

    /**
     * 一般请求
     *
     * @param service
     * @param <T>
     * @return
     */
    public static <T> T createRequest(Class<T> service) {
        Retrofit retrofit = getRetrofit(Api.API_HOST);
        if (!requestMaps.containsKey(service)) {
            T instance = retrofit.create(service);
            requestMaps.put(service, instance);
        }

        return (T) requestMaps.get(service);
    }


    /**
     * 自定义baseUrl
     *
     * @param service
     * @param baseUrl
     * @param <T>
     * @return
     */
    public static <T> T createRequest(Class<T> service, String baseUrl) {
        Retrofit retrofit = getRetrofit(baseUrl);
        T instance = retrofit.create(service);
        return instance;
    }
}