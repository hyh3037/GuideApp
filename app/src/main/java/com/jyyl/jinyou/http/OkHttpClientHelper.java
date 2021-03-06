package com.jyyl.jinyou.http;

import android.support.annotation.NonNull;

import com.jyyl.jinyou.MyApplication;
import com.jyyl.jinyou.utils.LogUtils;
import com.jyyl.jinyou.utils.NetUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * @Fuction: 配置Okhttp
 * @Author: Shang
 * @Date: 2016/5/18  9:04
 */
public class OkHttpClientHelper {

    //设缓存有效期为两天
    private static final long CACHE_STALE_SEC = 60 * 60 * 24 * 2;
    //查询缓存的Cache-Control设置，为if-only-cache时只查询缓存而不会请求服务器，max-stale可以配合设置缓存失效时间
    private static final String CACHE_CONTROL_CACHE = "only-if-cached, max-stale=" + CACHE_STALE_SEC;
    //查询网络的Cache-Control设置，头部Cache-Control设为max-age=0
    //(假如请求了服务器并在a时刻返回响应结果，则在max-age规定的秒数内，浏览器将不会发送对应的请求到服务器，数据由缓存直接返回)时则不会使用缓存而请求服务器
    private static final String CACHE_CONTROL_NETWORK = "max-age=0";

    private static volatile OkHttpClient sOkHttpClient;
    private static final int DEFAULT_TIMEOUT = 10;

    // 配置OkHttpClient
    public static OkHttpClient getOkHttpClient() {
        if (sOkHttpClient == null) {
            synchronized (HttpMethods.class) {
                if (sOkHttpClient == null) {
                    // OkHttpClient配置是一样的,静态创建一次即可
                    // 指定缓存路径,缓存大小100Mb
                    Cache cache = new Cache(new File(MyApplication.getInstance().getCacheDir(), "HttpCache"),
                            1024 * 1024 * 100);

                    sOkHttpClient = new OkHttpClient.Builder()
                            .cache(cache)
                            .addNetworkInterceptor(mRewriteCacheControlInterceptor)
                            .addInterceptor(mRewriteCacheControlInterceptor)
                            .addInterceptor(mLoggingInterceptor).retryOnConnectionFailure(true)
                            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS) //超时时间
                            .build();

                }
            }
        }
        return sOkHttpClient;
    }

    // 云端响应头拦截器，用来配置缓存策略
    private static Interceptor mRewriteCacheControlInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!NetUtils.isConnected(MyApplication.getInstance())) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
                LogUtils.e("no network");
            }
            Response originalResponse = chain.proceed(request);

            if (NetUtils.isConnected(MyApplication.getInstance())) {
                //有网的时候读接口上的@Headers里的配置，你可以在这里进行统一的设置
                String cacheControl = request.cacheControl().toString();
                return originalResponse.newBuilder().header("Cache-Control", cacheControl)
                        .removeHeader("Pragma").build();
            } else {
                return originalResponse.newBuilder()
                        .header("Cache-Control", "public, only-if-cached," + CACHE_STALE_SEC)
                        .removeHeader("Pragma").build();
            }
        }
    };

    // 打印返回的json数据拦截器
    private static Interceptor mLoggingInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {

            final Request request = chain.request();
            final Response response = chain.proceed(request);

            final ResponseBody responseBody = response.body();
            final long contentLength = responseBody.contentLength();

            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = Charset.forName("UTF-8");
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(charset);
                } catch (UnsupportedCharsetException e) {
                    LogUtils.e("");
                    LogUtils.e("Couldn't decode the response body; charset is likely malformed.");
                    return response;
                }
            }

            if (contentLength != 0) {
                LogUtils.v("--------------------------------------------开始打印返回数据----------------------------------------------------");
                LogUtils.d(buffer.clone().readString(charset));
                LogUtils.v("--------------------------------------------结束打印返回数据----------------------------------------------------");
            }

            return response;
        }
    };

    /**
     * 根据网络状况获取缓存的策略
     *
     * @return
     */
    @NonNull
    private String getCacheControl() {
        return NetUtils.isConnected(MyApplication.getInstance()) ? CACHE_CONTROL_NETWORK : CACHE_CONTROL_CACHE;
    }
}
