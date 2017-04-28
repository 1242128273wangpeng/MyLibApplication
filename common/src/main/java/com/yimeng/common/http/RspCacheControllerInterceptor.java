package com.yimeng.common.http;

import android.content.Context;

import com.apkfuns.logutils.LogUtils;
import com.yimeng.common.utils.NetWorkUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class RspCacheControllerInterceptor implements Interceptor {
    private final int maxTime = 60 * 60 * 24 * 7; // 缓存一周的时间
    private Context mContext;

    public RspCacheControllerInterceptor(Context context) {
        this.mContext = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response originalResponse = null;
        if (request.method().equals("GET")) {
            //要是没有网络的话我么就去缓存里面取数据
            if (!NetWorkUtils.isNetworkConnected(mContext)) {
                request = request.newBuilder()
                        .cacheControl(CacheControl.FORCE_CACHE)
                        .build();
                LogUtils.d("CacheInterceptor no network");
            }
            originalResponse = chain.proceed(request);
            if (NetWorkUtils.isNetworkConnected(mContext)) {
                /**
                 *
                 * Cache-Control 的值有很多, 最常用的是 max-age, 如果 response 带有 max-age, 则表示从现在起到 max-age 之前, 这个 response 都可以作为缓存使用 Server 的 Cache-Control
                 *
                 * .max-age:
                 * 请求:表示缓存的新鲜时间, 在此时间内可以不发送 http 请求去验证缓存而直接使用它,
                 * 如果 max-age=0. 则表示要求不要缓存文档.
                 * 其功能本质上与传统的Expires类似,但区别在于Expires是根据某个特定日期值做比较.
                 * 一但缓存者自身的时间不准确.则结果可能就是错误的.而max-age,显然无此问题.
                 * max-age的优先级也是高于Expires的.
                 */
                Response netResponse = originalResponse.newBuilder().
                        removeHeader("Pragma")// 清除头信息，因为服务器如果不支持，会返回一些干扰信息，不清除下面无法生效
                        .header("Cache-Control", "public, max-age=" + 0) //这里设置的为0就是说不进行缓存
                        .build();
                return netResponse;
            } else {
                /**
                 * Client 的 Cache-Control max-stale, 可以使用过期缓存.only-if-cached, 只想要已缓存的数据, 否则返回 504 503
                 *
                 * .max-stale:
                 *请求:意思是,我允许缓存者，可以发送一个过期缓存,过期不超过指定秒数的,陈旧的缓存.
                 * 响应:同上.
                 */
                Response cacheResponse = originalResponse.newBuilder()
                        .removeHeader("Pragma")
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxTime)//这里的设置的是我们的没有网络的缓存时间
                        .build();
                return cacheResponse;
            }
        } else { // post请求不做缓存设置
            return originalResponse;
        }
    }
}
