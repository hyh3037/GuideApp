package com.jyyl.guideapp.http;

import com.jyyl.guideapp.entity.DeviceInfo;

import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @Fuction: 对retrofit实例化的简单封装,包含同一个BASE_URL所有网络交互的方法
 * @Author: Shang
 * @Date: 2016/5/18  14:25
 */
public class HttpMethods {

    private ApiService mApiService;

    private static volatile HttpMethods instance = null;

    //私有构造方法
    private HttpMethods() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Api.BASE_URL)
                .client(OkHttpClientHelper.getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();

        mApiService = retrofit.create(ApiService.class);
    }

    /**
     * 获取单例
     * @return 实例
     */
    public static HttpMethods getInstance() {

        // if already inited, no need to get lock everytime
        if (instance == null) {
            synchronized (HttpMethods.class) {
                if (instance == null) {
                    instance = new HttpMethods();
                }
            }
        }

        return instance;
    }

    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     * @param <T>
     *         Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            if (httpResult.getResultCode() != ResultCode.HTTP_SUCCESS) {
                throw new ApiException(httpResult.getResultCode());
            }
            return httpResult.getData();
        }
    }

    public void getDeviceInfo(Subscriber<List<DeviceInfo>> subscriber, String phone) {

        mApiService.getDeviceInfo(phone)
                .map(new HttpResultFunc<List<DeviceInfo>>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

    }
}
