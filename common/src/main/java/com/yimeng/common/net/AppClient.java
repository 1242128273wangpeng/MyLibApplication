package com.yimeng.common.net;

import android.content.Context;

import com.yimeng.common.BuildConfig;
import com.yimeng.common.http.GlobeHttpHandler;
import com.yimeng.common.http.RequestIntercept;
import com.yimeng.common.http.RspCacheControllerInterceptor;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppClient {
    private static String mBaseUrl;
    private static Context mContext;
    public static Retrofit mRetrofit;
    public static HashMap<String, Object> clientMap = new HashMap<String, Object>();

    public static Retrofit retrofit() {
        if (mRetrofit == null) {
            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.retryOnConnectionFailure(true);
            if (BuildConfig.DEBUG) {
                builder.addNetworkInterceptor(getRequestIntercept());
            }
            builder.addNetworkInterceptor(new RspCacheControllerInterceptor(mContext));
            builder.connectTimeout(12, TimeUnit.SECONDS).readTimeout(12, TimeUnit.SECONDS);
            OkHttpClient okHttpClient = builder.build();
            mRetrofit = new Retrofit.Builder()
                    .baseUrl(mBaseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return mRetrofit;
    }

    public static <T> T getInstance(Class<T> t) {
        T instance = null;
        if (clientMap.containsKey(t.getSimpleName())) {
            instance = (T) clientMap.get(t.getSimpleName());
        } else {
            instance = retrofit().create(t);
            clientMap.put(t.getSimpleName(), instance);
        }
        return instance;
    }

    public static void init(Context context, String baseUrl) {
        mContext = context;
        mBaseUrl = baseUrl;
    }

    // 日志拦截器
    public static RequestIntercept getRequestIntercept() {
        RequestIntercept requestIntercept = new RequestIntercept(new GlobeHttpHandler() {
            @Override
            public Response onHttpResultResponse(String httpResult, Interceptor.Chain chain, Response response) {
                //这里可以比客户端提前一步拿到服务器返回的结果,可以做一些操作,比如token超时,重新获取
                return response;
            }

            @Override
            public Request onHttpRequestBefore(Interceptor.Chain chain, Request request) {
                //在请求服务器之前可以拿到request,做一些操作比如给request添加header,如果不做操作则返回参数中的request
                return request;
            }
        });
        return requestIntercept;
    }


}
