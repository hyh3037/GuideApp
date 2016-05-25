package com.jyyl.guideapp.http;


import com.jyyl.guideapp.entity.DeviceInfo;

import java.util.List;
import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * @Fuction: 请求数据服务，这里是所有网络请求的接口方法
 * @Author: Shang
 * @Date: 2016/4/11  14:21
 */
public interface ApiService {


    //获取验证码
    @GET("http://1y5a133877.iask.in:19425/tourism/messageauth/{phone}&{type}&{industry}")
    Observable<HttpResult<String>> getSecurityCode(@Path("phone") String phone,
                                                   @Path("type") String type,
                                                   @Path("industry") String industry);

    //注册
    @POST(Api.REGISTER_URL)
    Observable<HttpResult<String>> registerAccount(@Body Map<String, String> params);

    //登录
    @GET(Api.LOGIN_URL + "/{loginTime}&{memberAccount}&{memberPassword}&{industry}")
    Observable<HttpResult<String>> loginAccount(@Path("loginTime") String loginTime,
                                                @Path("memberAccount") String memberAccount,
                                                @Path("memberPassword") String memberPassword,
                                                @Path("industry") String industry);

    //获取导游所有设备
    @POST(Api.USER_DEVICES_URL)
    Observable<HttpResult<String>> getUserDevices(@Body Map<String, String> params);

    @GET("platformenter/platformenter/registered")
    Observable<HttpResult<List<DeviceInfo>>> getDeviceInfo(@Query("phone") String phone);
}
