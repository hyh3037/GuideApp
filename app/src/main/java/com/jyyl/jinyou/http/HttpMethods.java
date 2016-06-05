package com.jyyl.jinyou.http;

import android.content.Context;

import com.jyyl.jinyou.MyApplication;
import com.jyyl.jinyou.constans.Sp;
import com.jyyl.jinyou.entity.DeviceResult;
import com.jyyl.jinyou.entity.LoginResult;
import com.jyyl.jinyou.entity.VersionInfo;
import com.jyyl.jinyou.utils.LogUtils;
import com.jyyl.jinyou.utils.SPUtils;
import com.jyyl.jinyou.utils.TimeUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @Fuction: 对retrofit实例化的简单封装, 包含同一个BASE_URL所有网络交互的方法
 * @Author: Shang
 * @Date: 2016/5/18  14:25
 */
public class HttpMethods {

    private static String TAG = "HttpMethods";

    private ApiService mApiService;
    private Context appContext;

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

        appContext = MyApplication.getInstance().getApplicationContext();
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
    private class HttpResultListFunc<T> implements Func1<HttpResultList<T>, List<T>> {

        @Override
        public List<T> call(HttpResultList<T> httpResult) {
            LogUtils.d(TAG, httpResult.toString());
            if (!(ResultStatus.HTTP_SUCCESS).equals(httpResult.getStatus())) {
                throw new ApiException(httpResult.getDescritpion());
            }else {
                return httpResult.getValues();
            }
        }
    }

    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            LogUtils.d(TAG, httpResult.toString());
            if ((ResultStatus.HTTP_SUCCESS).equals(httpResult.getStatus())) {
                return httpResult.getValues();
            }else {
                throw new ApiException(httpResult.getDescritpion());
            }
        }
    }

    /**=====================================网络请求方法=========================================*/

    /**
     * 获取验证码
     * @param phone
     * @return
     */
    public Observable<Object> getSecurityCode(String phone) {
        return mApiService.getSecurityCode(phone, "0", "jy")
                .map(new HttpResultFunc<>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * 注册
     * @param memberAccount
     * @param memberPassword
     * @param messInfo
     * @return
     */
    public Observable<Object> registerAccount(String memberAccount,String memberPassword,String messInfo) {

        Map<String, String> params = new HashMap<>();
        params.put("industry", "jy");
        params.put("memberAccount", memberAccount);
        params.put("memberPassword", memberPassword);
        params.put("messInfo", messInfo);
        params.put("memberType", "1");
        params.put("memberPhone", memberAccount);

        return mApiService.registerAccount(params)
                .map(new HttpResultFunc<>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * 登录
     * @param memberAccount
     * @param memberPassword
     * @return
     */
    public Observable<List<LoginResult>> loginAccount(String memberAccount,String memberPassword) {

        String loginTime = TimeUtils.getStringDate();
        LogUtils.d(loginTime);

        return mApiService.loginAccount(loginTime,memberAccount,memberPassword,"jy" )
                .map(new HttpResultListFunc<LoginResult>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }


    /**
     * 上传导游位置
     * @return
     */
    public Observable<List<String>> uploadLocation(String longitude, String latitude, String locationTime) {

        Map<String, String> params = new HashMap<>();
        String userId = (String) SPUtils.get(appContext, Sp.SP_KEY_USER_ID,"-1");
        params.put("memberId", userId);
        LogUtils.d(TAG,userId);
        params.put("longitude",longitude);
        params.put("latitude",latitude);
        params.put("locationTime",locationTime);
        params.put("industry","jy");

        return mApiService.uploadLocation(params)
                .map(new HttpResultListFunc<String>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * 上传导游信息
     * @return
     */
    public Observable<List<String>> uploadGuideInfo(String guideCard) {

        Map<String, String> params = new HashMap<>();
        String userId = (String) SPUtils.get(appContext, Sp.SP_KEY_USER_ID,"-1");
        params.put("guideId",userId);
        params.put("guideCard",guideCard);
        params.put("guideCardAddress","");
        params.put("guideWorkTime","");
        params.put("guideNote","");

        return mApiService.uploadGuideInfo(params)
                .map(new HttpResultListFunc<String>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * 查询导游详细信息
     * @return
     */
    public Observable<List<String>> getGuideInfo() {

        Map<String, String> params = new HashMap<>();
        String userId = (String) SPUtils.get(appContext, Sp.SP_KEY_USER_ID,"-1");
        params.put("guideId", userId);

        return mApiService.getGuideInfo(params)
                .map(new HttpResultListFunc<String>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }



    /**
     * 查询导游所拥有的设备
     * @return
     */
    public Observable<List<DeviceResult>> getUserDevices() {

        Map<String, String> params = new HashMap<>();
        String userId = (String) SPUtils.get(appContext, Sp.SP_KEY_USER_ID,"-1");
        params.put("ascriptionId",userId);

        return mApiService.getUserDevices(params)
                .map(new HttpResultListFunc<DeviceResult>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * 查询导游所拥有未绑定的设备
     * @return
     */
    public Observable<List<DeviceResult>> getUserNotBoundDevices() {

        Map<String, String> params = new HashMap<>();
        String userId = (String) SPUtils.get(appContext, Sp.SP_KEY_USER_ID,"-1");
        params.put("ascriptionId",userId);
        params.put("bindState","0");

        return mApiService.getUserDevices(params)
                .map(new HttpResultListFunc<DeviceResult>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }



    /**
     * 查询版本信息
     * @param versioncode
     * @return
     */
    public Observable<List<VersionInfo>> updateVersion(String versioncode) {
        return mApiService.updateVersion(versioncode, "jytourist", "jy")
                .map(new HttpResultListFunc<VersionInfo>())
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
}
